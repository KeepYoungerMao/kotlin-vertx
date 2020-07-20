package com.mao.service.data

import com.mao.server.ApiServer
import com.mao.service.BaseService
import com.mao.entity.data.DataColumn
import com.mao.entity.data.DataTable
import com.mao.entity.data.DataType
import com.mao.sql.Query
import com.mao.util.SU
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.IllegalArgumentException

/**
 * data类型数据的增删改查请求处理
 * 采用统一格式的请求方式
 * 要求data数据类型遵循格式要求
 */
class DataServiceImpl : DataService, BaseService() {

    companion object {
        const val DATA = "data"
        const val TYPE = "type"

        const val SRC_TYPE = "src"
        const val LIST_TYPE = "list"
        const val PAGE_TYPE = "page"
        const val CLASSIFY_TYPE = "classify"
    }

    private val query : Query = Query.INSTANCE

    /**
     * 主表支持：列表搜索、分页查询、详情查询、字典查询
     * 副表支持：列表查询、详情查询
     * 保存支持：单个保存，多个保存
     * 更新支持：单个更新
     * 删除支持：暂时不开放
     *
     * 请求根据 HttpMethod进行区分：
     * GET      查询
     * POST     更新
     * PUT      保存
     * DELETE   删除
     *
     * 数据表结构约束：
     * 表内必须含有字段：
     * id       作为主键（long型），由系统生成
     * update   作为更新标识（long型），由系统生成，13位时间戳
     * delete   删除标识（Boolean型），由系统生成，默认false
     * 详情查询为根据主键id查询，但没有硬性规定，但趋向于主键id
     * 更新请求必须根据主键id进行更新，硬性规定
     * 表字段类型必须再规定范围内（以MYSQL为例）：
     * DataType类型       MySQL类型       java类型
     * INT                int(10)         Integer
     * BIGINT             bigint(20)      Long
     * STRING             varchar(..)     String
     * TEXT               text            String
     * UPDATE             bigint(13)      Long
     * BOOLEAN            tinyint(1)      Boolean
     * 其中字段长度可根据自身长度设置，text,mediumtext,longBlog等都归为TEXT，通过长度设置来限制
     */
    override fun handle(ctx: RoutingContext) {
        val data: String = ctx.pathParam(DATA)
        val type: String = ctx.pathParam(TYPE)
        val table: DataTable? = getDataTable(data)
        if (null == table)
            refuse(ctx,"invalid data type[ $data ]")
        else {
            when (ctx.request().method()) {
                HttpMethod.GET -> {
                    when (type) {
                        SRC_TYPE -> searchSrc(ctx, table)
                        LIST_TYPE -> searchList(ctx, table)
                        PAGE_TYPE -> {
                            if (table.main) {
                                searchPage(ctx, table)
                            } else {
                                dns(ctx,data,type)
                            }
                        }
                        CLASSIFY_TYPE -> {
                            if (table.main) {
                                searchClassify(ctx, table)
                            } else {
                                dns(ctx,data,type)
                            }
                        }
                        else -> dns(ctx,data,type)
                    }
                }
                HttpMethod.PUT -> {
                    when (type) {
                        SRC_TYPE -> saveSrc(ctx, table)
                        LIST_TYPE -> saveList(ctx, table)
                        else -> dns(ctx,data,type)
                    }
                }
                HttpMethod.POST -> {
                    when (type) {
                        SRC_TYPE -> updateSrc(ctx,table)
                        else -> dns(ctx,data,type)
                    }
                }
                HttpMethod.DELETE -> {
                    when (type) {
                        SRC_TYPE -> noOpen(ctx)
                        LIST_TYPE -> noOpen(ctx)
                        else -> dns(ctx,data,type)
                    }
                }
                else -> mns(ctx)
            }
        }
    }

    /**
     * 保存数据
     * INSERT INTO [table] ( columns ) VALUE ( ... )
     * 保存的SQL语句会对所有字段进行保存，没有传输的字段如果是必须的则抛出异常，如果非必须则放默认值
     * 默认：STRING：null，TEXT：null，INT：0，BIGINT：0，BOOLEAN：0
     * 对于数据库有设置默认值的情况不予考虑
     */
    private fun saveSrc(ctx: RoutingContext, table: DataTable) {
        //获取body数据是格式错误会抛出DecodeException
        val obj: JsonObject? = try {
            ctx.bodyAsJson
        } catch (e: Exception) {
            null
        }
        if (null == obj)
            err(ctx,"not found request body data or it is not a object data.")
        else {
            //拼接保存语句后部分
            val sqlEnd = saveSqlEnd(obj, table)
            val sqlColumn = saveSqlColumns(table)
            val sql = "INSERT INTO ${table.table}$sqlColumn VALUE $sqlEnd"
            //执行语句
            sqlResult(ctx,sql,single = true,commit = true)
        }
    }

    /**
     * 保存多个数据
     */
    private fun saveList(ctx: RoutingContext, table: DataTable) {
        //获取body数据时格式错误会抛出DecodeException
        val array: JsonArray? = try {
            ctx.bodyAsJsonArray
        } catch (e: Exception) { null }
        if (null == array || array.isEmpty)
            err(ctx, "not found request body or it is not a objectArray data")
        else {
            //多个保存语句进行拼接
            val size = array.size()
            var sqlEnd = ""
            for (i in 0 until size) {
                sqlEnd += saveSqlEnd(array.getJsonObject(i),table)
                if (i != size - 1)
                    sqlEnd += ","
            }
            val sqlColumn = saveSqlColumns(table)
            val sql = "INSERT INTO ${table.table}$sqlColumn VALUES $sqlEnd"
            //执行语句
            sqlResult(ctx,sql,single = false,commit = true)
        }
    }

    /**
     * 保存SQL语句的后面部分
     * 循环table.columns，从JsonObject获取值进行拼接
     * 拼接过程中对字段进行判断
     */
    private fun saveSqlEnd(obj: JsonObject, table: DataTable) : String {
        val sb = StringBuilder()
        sb.append("(")
        table.columns.forEach { column -> kotlin.run {
            val name:String = column.column
            when (column.type) {
                //int类型：默认0，如果字段必须，不传或传入0则抛异常（不考虑字段是否允许0值）
                DataType.INT -> {
                    val value: Int = if (column.save)
                        try {
                            obj.getInteger(name) !!
                        } catch (e: Exception) {
                            necessary(name)
                            0
                        }
                    else
                        obj.getInteger(name,0)
                    if (column.save && value == 0)
                        necessary(name)
                    sb.append("$value,")
                }
                //long类型：默认0，如果字段必须，不传或传入0则抛异常（不考虑字段是否允许0值）
                //所有主键id名【id】，且为long型字段，由系统生成。
                DataType.BIGINT -> {
                    if (name == ID) {
                        val id = ApiServer.idBuilder.nextId()
                        sb.append("$id,")
                    } else {
                        val value: Long = if (column.save)
                            try {
                                obj.getLong(name) !!
                            } catch (e: Exception) {
                                necessary(name)
                                0L
                            }
                        else
                            obj.getLong(name,0L)
                        if (column.save && value == 0L)
                            necessary(name)
                        sb.append("$value,")
                    }
                }
                //String类型：默认null，如果字段必须，不传或传空则抛出异常，长度超出设定值抛出异常
                DataType.STRING -> {
                    val value: String? = if (column.save) {
                        val a = try {
                            obj.getString(name)!!
                        } catch (e: Exception) {
                            necessary(name)
                            null
                        }
                        when {
                            null == a || a.isEmpty() -> {
                                necessary(name)
                                null
                            }
                            (a.length > column.len) -> {
                                tooLong(name)
                                null
                            }
                            else -> a
                        }
                    } else
                        obj.getString(name,null)
                    if (null == value)
                        sb.append("NULL,")
                    else
                        sb.append("'$value',")
                }
                //String类型，文本字段，与String相同判断，不过长度比较的是byte大小
                DataType.TEXT -> {
                    val value: String? = if (column.save) {
                        val a = try {
                            obj.getString(name) !!
                        } catch (e: Exception) {
                            necessary(name)
                            null
                        }
                        when {
                            null == a || a.isEmpty() -> {
                                necessary(name)
                                null
                            }
                            large(a,column.len) -> {
                                tooLong(name)
                                null
                            }
                            else -> a
                        }
                    } else
                        obj.getString(name,null)
                    if (null == value)
                        sb.append("NULL,")
                    else
                        sb.append("'$value',")
                }
                //特殊字段，时间戳，保存时直接赋值，不做判断
                DataType.UPDATE -> sb.append("${SU.now()},")
                //布尔值，传错或不传不做处理，使用默认值
                DataType.BOOLEAN -> {
                    val value: Boolean = if (column.save)
                        try {
                            obj.getBoolean(name) !!
                        } catch (e: Exception) {
                            necessary(name)
                            false
                        }
                    else
                        obj.getBoolean(name,column.len == 1)
                    sb.append("$value,")
                }
            }
        } }
        val s = sb.toString()
        val t = s.substring(0,s.lastIndex)
        return "$t)"
    }

    /**
     * 保存sql语句的保存字段部分
     * 循环table，取所有字段字段
     */
    private fun saveSqlColumns(table: DataTable) : String {
        val sb = StringBuilder()
        sb.append("(")
        table.columns.forEach { sb.append("`${it.column}`,") }
        val sql = sb.toString()
        val sql2 = sql.substring(0,sql.lastIndex)
        return "$sql2)"
    }

    /**
     * 更新数据
     */
    private fun updateSrc(ctx: RoutingContext, table: DataTable) {
        //获取body数据是格式错误会抛出DecodeException
        val obj: JsonObject? = try {
            ctx.bodyAsJson
        } catch (e: Exception) {
            null
        }
        if (null == obj)
            err(ctx,"not found request body data or it is not a object data.")
        else {
            val sb = StringBuilder()
            var where = ""
            var update = ""
            for (column in table.columns) {
                val name = column.column
                when (column.type) {
                    DataType.INT -> {
                        val value: Int? = obj.getInteger(name,null)
                        if (null != value) {
                            if (value <= 0)
                                invalid(name)
                            else
                                sb.append("`$name` = $value, ")
                        }
                    }
                    DataType.BIGINT -> {
                        val value: Long? = obj.getLong(name,null)
                        if (name == ID) {
                            if (null == value || value <= 0)
                                invalid(name)
                            else
                                where = "WHERE `$name` = $value"
                        } else {
                            if (null != value) {
                                if (value <= 0)
                                    invalid(name)
                                else
                                    sb.append("`$name` = $value, ")
                            }
                        }
                    }
                    //更新时支持更新为空字符串，但不允许更新null
                    DataType.STRING -> {
                        val value: String? = obj.getString(name,null)
                        if (null != value) {
                            if (value.length > column.len)
                                tooLong(name)
                            else
                                sb.append("`$name` = '$value', ")
                        }
                    }
                    DataType.TEXT -> {
                        val value: String? = obj.getString(name,null)
                        if (null != value) {
                            if (value.toByteArray().size > column.len.times(1024))
                                tooLong(name)
                            else
                                sb.append("`$name` = '$value', ")
                        }
                    }
                    DataType.BOOLEAN -> {
                        val value: Boolean? = obj.getBoolean(name,null)
                        if (null != value) {
                            sb.append("`$name` = $value, ")
                        }
                    }
                    DataType.UPDATE -> update = "`$name` = ${SU.now()}"
                }
            }
            when {
                sb.isEmpty() -> err(ctx,"no data to update.")
                where == "" -> err(ctx,"need primary key[ id ] while updating data.")
                else -> {
                    val sql = "UPDATE ${table.table} SET $sb $update $where"
                    //执行语句
                    sqlResult(ctx,sql,single = true,commit = true)
                }
            }
        }
    }

    /**
     * 查询详情
     * 规定：使用id主键进行详情查询
     * 找出table中 src_key = true 的字段，只存在一个字段，存在多个则为数据错误
     * 找出table中 src_show = true 的字段， 查询这些字段
     */
    private fun searchSrc(ctx: RoutingContext, table: DataTable) {
        val id = SU.toLong(ctx.request().getParam("id"))
        if (id <= 0)
            err(ctx,"invalid param[id].")
        else {
            val sb = StringBuilder()
            var key = ""
            table.columns.forEach { column -> kotlin.run {
                if (column.src_key)
                    key = column.column
                if (column.src_show)
                    sb.append("`${column.column}`,")
            } }
            val sqlC = sb.toString()
            val sql = "SELECT ${sqlC.substring(0,sqlC.length.dec())} FROM ${table.table} WHERE `$key` = $id"
            sqlResult(ctx,sql,single = true,commit = false)
        }
    }

    /**
     * 查询列表
     * 1. 主表的列表查询：根据关键词查询列表
     * 2. 副表的列表查询：根据其主表的主键查询该副表中关联主键的所有列表数据
     */
    private fun searchList(ctx: RoutingContext, table: DataTable) {
        //用于记录查询的字段
        val sb = StringBuilder()
        if (table.main) {
            //主表支持条数限制
            val row = SU.toLong(ctx.request().getParam("row"),10)
            //收集可用于搜索的字段
            val listKeys = ArrayList<DataColumn>()
            table.columns.forEach { column -> kotlin.run {
                //收集查询的字段
                if (column.list_show)
                    sb.append("`${column.column}`,")
                //收集用于搜索的字段
                if (column.list_key)
                    listKeys.add(column)
            } }
            //判断参数，多个参数搜索sql之间需要加 AND
            var first = true
            //记录查询字段部分sql
            var sqlK = ""
            //根据得到的可用于搜索的字段寻找url参数，寻找到了则拼接进sql
            for (a in listKeys){
                val kw = ctx.request().getParam(a.column)
                val item = if (null == kw || kw.isEmpty()) {
                    ""
                } else {
                    //判断字段类型，数字类型参数和字符串类型参数需要分开做处理
                    when (a.type) {
                        DataType.INT, DataType.BIGINT -> "`${a.column}` = $kw"
                        DataType.STRING, DataType.TEXT -> "LOCATE('$kw',`${a.column}`) > 0 "
                        DataType.BOOLEAN -> {
                            if (kw.toUpperCase() == "TRUE")
                                "`${a.column}` = TRUE"
                            else
                                "`${a.column}` = FALSE"
                        }
                        DataType.UPDATE -> ""
                    }
                }
                if (item != "") {
                    sqlK += if (first) " $item " else " AND $item "
                    first = !first
                }
            }
            //如果没有查询字段，则错误返回
            if (sqlK == "")
                err(ctx,"at least one param to search data.")
            else {
                val sqlC = sb.toString()
                val sqlD = sqlC.substring(0,sqlC.length.dec())
                val sql = "SELECT $sqlD FROM ${table.table} WHERE $sqlK ${orderSql(table)} LIMIT $row"
                sqlResult(ctx,sql,single = false,commit = false)
            }
        } else {
            //获取主表id。副表（如章节表）的列表查询为查询等于pid的所有列表
            val pid = SU.toLong(ctx.request().getParam(table.main_id))
            if (pid <= 0)
                err(ctx,"invalid param[${table.main_id}].")
            else {
                table.columns.forEach { column -> kotlin.run {
                    if (column.list_show)
                        sb.append("`${column.column}`,")
                } }
                val sqlC = sb.toString()
                val sqlD = sqlC.substring(0,sqlC.length.dec())
                val sql = "SELECT $sqlD FROM ${table.table} WHERE `${table.main_id}` = $pid ${orderSql(table)}"
                sqlResult(ctx,sql,single = false,commit = false)
            }
        }
    }

    /**
     * 分页查询：只支持主表
     * 分页查询参数：page、row
     */
    private fun searchPage(ctx: RoutingContext, table: DataTable) {
        val page = SU.toLong(ctx.request().getParam("page"),1)
        val row = SU.toLong(ctx.request().getParam("row"),20)
        val sb = StringBuilder()
        val pageKeys: MutableList<DataColumn> = ArrayList()
        table.columns.forEach { column -> kotlin.run {
            //收集查询的字段
            if (column.page_show)
                sb.append("`${column.column}`,")
            //收集用于搜索的字段
            if (column.page_key)
                pageKeys.add(column)
        } }
        val sqlC = sb.toString()
        val sqlD = sqlC.substring(0,sqlC.length.dec())
        var first = true
        var sqlK = ""
        for (item in pageKeys) {
            val i = ctx.request().getParam(item.column)
            val k: String = if (null == i || i.isEmpty()) {
                ""
            } else {
                when (item.type) {
                    //分页查询最常用支持数字参数
                    DataType.INT, DataType.BIGINT -> {
                        val ii = SU.toLong(i)
                        if (ii <= 0) ""
                        else "`${item.column}` = $ii"
                    }
                    //分页查询支持bool参数，接收true、false
                    DataType.BOOLEAN -> {
                        when {
                            i.toUpperCase() == "TRUE" -> "`${item.column}` = TRUE"
                            i.toUpperCase() == "FALSE" -> "`${item.column}` = FALSE"
                            else -> ""
                        }
                    }
                    //分页查询严格来说不支持字符串参数，但考虑到：A、B、C这种参数
                    DataType.STRING -> "`${item.column}` = '$i'"
                    else -> ""
                }
            }
            if (k != "") {
                sqlK += if (first) " $k " else " AND $k "
                first = !first
            }
        }
        val pageC = if (page < 1) 0 else page.dec().times(row)
        val sql = if (sqlK == "")
            "SELECT $sqlD FROM ${table.table} ${orderSql(table)} LIMIT $pageC,$row"
        else
            "SELECT $sqlD FROM ${table.table} WHERE $sqlK ${orderSql(table)} LIMIT $pageC,$row"
        sqlResult(ctx,sql,single = false,commit = false)
    }

    /**
     * 字典查询：只支持主表
     * 根据字符串匹配表名称，匹配不成功返回null
     */
    private fun searchClassify(ctx: RoutingContext, table: DataTable) {
        val sql = "SELECT * FROM tt_data_dict WHERE `data` = '${table.name.toUpperCase()}'"
        sqlResult(ctx,sql,single = false,commit = false)
    }

    /**
     * 排序部分SQL
     */
    private fun orderSql(table: DataTable) : String {
        var orderSql = ""
        for (column: DataColumn in table.columns) {
            when (column.order) {
                1 -> orderSql += "${column.column} ASC,"
                2 -> orderSql += "${column.column} DESC,"
            }
        }
        return if (orderSql == "")
            ""
        else
            "ORDER BY ${orderSql.substring(0,orderSql.length.dec())}"
    }

    /**
     * 获取表数据
     */
    private fun getDataTable(name: String) : DataTable? {
        ApiServer.dataTable.forEach { table -> if (table.name == name) return table }
        return null
    }

    /**
     * data type not support
     * 表示请求该数据的时候类型错误，不支持该类型
     */
    private fun dns(ctx: RoutingContext, data: String, type: String) {
        refuse(ctx,"table[ $data ] not support search type[ $type ].")
    }

    /**
     * request method not support
     * 表示请求方法类型不支持，只支持：GET、POST、PUT、DELETE
     */
    private fun mns(ctx: RoutingContext) {
        refuse(ctx,"request method[ ${ctx.request().method().name} ] not support.")
    }

    /**
     * 提示服务暂不可用
     */
    private fun noOpen(ctx: RoutingContext) {
        refuse(ctx,"The service is temporarily unavailable.")
    }

    /**
     * 异常抛出，输入的字段值缺失或错误，用于保存SQL
     */
    private fun necessary(name: String) {
        throw IllegalArgumentException("column[ $name ] is necessary while saving data. or it it wrong value")
    }

    /**
     * 异常抛出，输入的字段值错误，用于更新SQL
     */
    private fun invalid(name: String) {
        throw IllegalArgumentException("column[ $name ] is necessary while updating data. or it it wrong value")
    }

    /**
     * 异常抛出，字段值太长，再检查字符串类型字段时抛出
     */
    private fun tooLong(name: String) {
        throw IllegalArgumentException("column[ $name ] is too long, cannot execute this data.")
    }

    /**
     * 判断字符串类型字段值的大小是否大于kb值
     * 比较byte大小
     */
    private fun large(str: String, kb: Int): Boolean {
        val a = str.toByteArray().size
        return a > (kb.times(1024))
    }

    /**
     * 统一执行sql语句，并操作RoutingContext返回数据
     * 执行sql语句也是同一调用execute方法
     * @param ctx RoutingContext上下文
     * @param sql 执行的SQL语句
     * @param single 在SQL语句为查询类语句时使用，为true时表示只查询一条记录，false表示查询多条记录
     * @param commit 是否需要提交，需要提交的表示为更新、保存、删除类型SQL语句
     */
    private fun sqlResult(ctx: RoutingContext, sql: String, single: Boolean, commit: Boolean) {
        println("sql: $sql")
        query.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ok(ctx,res.result())
            } else {
                err(ctx,"query database error.")
            }
        } })
    }

}