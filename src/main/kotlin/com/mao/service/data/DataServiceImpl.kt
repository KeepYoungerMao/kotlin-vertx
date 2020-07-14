package com.mao.service.data

import com.mao.server.ApiServer
import com.mao.service.BaseService
import com.mao.service.data.entity.DataColumn
import com.mao.service.data.entity.DataTable
import com.mao.service.data.entity.DataType
import com.mao.sql.Query
import com.mao.util.SU
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

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
     */
    private fun saveSrc(ctx: RoutingContext, table: DataTable) {
        TODO("on building...")
    }

    /**
     * 保存多个数据
     */
    private fun saveList(ctx: RoutingContext, table: DataTable) {
        TODO("on building...")
    }

    /**
     * 更新数据
     */
    private fun updateSrc(ctx: RoutingContext, table: DataTable) {
        TODO("on building...")
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
                        DataType.STRING, DataType.TEXT -> "`${a.column}` = '$kw'"
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
                val sql = "SELECT $sqlD FROM ${table.table} WHERE $sqlK LIMIT $row"
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
                val sql = "SELECT $sqlD FROM ${table.table} WHERE `${table.main_id}` = $pid"
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
            "SELECT $sqlD FROM ${table.table} LIMIT $pageC,$row"
        else
            "SELECT $sqlD FROM ${table.table} WHERE $sqlK LIMIT $$pageC,$row"
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
     * 获取表数据
     */
    private fun getDataTable(name: String) : DataTable? {
        ApiServer.dataTable.forEach { table -> if (table.name == name) return table }
        return null
    }

    private fun sqlPf(sql: String) {
        println("sql execute: $sql")
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
     * 统一执行sql语句，并操作RoutingContext返回数据
     * 执行sql语句也是同一调用execute方法
     * @param ctx RoutingContext上下文
     * @param sql 执行的SQL语句
     * @param single 在SQL语句为查询类语句时使用，为true时表示只查询一条记录，false表示查询多条记录
     * @param commit 是否需要提交，需要提交的表示为更新、保存、删除类型SQL语句
     */
    private fun sqlResult(ctx: RoutingContext, sql: String, single: Boolean, commit: Boolean) {
        query.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ok(ctx,res.result())
            } else {
                err(ctx,"query database error.")
            }
        } })
    }

}