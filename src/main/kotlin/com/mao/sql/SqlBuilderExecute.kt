package com.mao.sql

import com.mao.data.Column
import com.mao.data.DataTable
import com.mao.type.DataType
import com.mao.server.ApiServer
import com.mao.util.SU
import io.vertx.core.MultiMap
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

/**
 * 组装SQL的基本操作实现
 * 在组装SQL的同时会对传递的参数做基本的校验，
 * 如果不符合校验则抛出IllegalArgumentException异常。
 */
class SqlBuilderExecute : SqlBuilder() {

    /**
     * 使用单个参数查询
     * 单个参数查询分两种：
     * 一种是查询该参数下的表的某个数据详情：比如id查询book详情
     * 一种是查询该参数分类下的所有数据列表：比如book_id查询该id下的章节列表
     * 这种需要该表不为顶级表
     */
    override fun dataSearch(p: String?, table: DataTable, list: Boolean) : String {
        val id = SU.toLong(p)
        if (id < 0)
            throw IllegalArgumentException("invalid param id")
        return if (list){
            //用于查询所有章节列表，获取字段下list参数为true的字段
            if (table.main)
                throw IllegalArgumentException("not support search list data with key column. it isn't a main table.")
            "SELECT ${dataShow(table)} FROM ${table.name} WHERE `${dataListKey(table)}` = $id ORDER BY `${table.order}`"
        } else {
            //查询单个详情，获取主键
            "SELECT * FROM ${table.name} WHERE `${dataKey(table)}` = $id"
        }
    }

    /**
     * 获取查询次级表时的顶级表主键
     */
    private fun dataListKey(table: DataTable) : String {
        table.columns.forEach { if (it.list) return it.name }
        throw IllegalArgumentException("search database error: not found list key column.")
    }

    /**
     * 获取表中主键
     * 获取不到抛出异常
     */
    private fun dataKey(table: DataTable) : String {
        table.columns.forEach { if (it.type == Column.KEY) return it.name }
        throw IllegalArgumentException("search database error: not found key column.")
    }

    /**
     * 获取查询列表时的展现字段的部分SQL
     */
    private fun dataShow(table: DataTable) : String {
        var sql = ""
        table.columns.forEach { if (it.show) sql += "`${it.name}`," }
        if (SU.isEmpty(sql))
            throw IllegalArgumentException("not found any show column while search list data.")
        return sql.substring(0,sql.lastIndex)
    }

    /**
     * 多个数据查询
     * 多个数据查询分为：
     * 检索查询：获取list字段为true的
     * 分页查询：获取page字段为true的
     * 展现show字段，使用order字段进行排序
     * 次级表暂不支持检索查询和分页查询（因为list字段用在了别的用途）
     */
    override fun dataSearch(map: MultiMap, table: DataTable, list: Boolean) : String {
        if (!table.main)
            throw IllegalArgumentException("not support search list data when it is not main table.")
        val row = SU.toLong(map["row"],20)
        if (list) {
            var sql = ""
            table.columns.forEach { column -> kotlin.run {
                if (column.list) {
                    val param = map[column.name]
                    if (SU.isNotEmpty(param)) {
                        when (column.type) {
                            Column.STRING -> sql += "AND `${column.name}` = '$param' "
                            else -> throw IllegalArgumentException("search database error: list search param type must be string.")
                        }
                    }
                }
            } }
            if (SU.isEmpty(sql))
                throw IllegalArgumentException("no search param. if you send, it maybe database json no set.")
            return "SELECT ${dataShow(table)} FROM ${table.name} WHERE `delete` = FALSE $sql ORDER BY `${table.order}` LIMIT $row"
        } else {
            //分页查询将字段检查放宽，错误的不追究，需要的自动默认值填补
            val page = SU.toLong(map["page"],0)
            var sql = ""
            table.columns.forEach { column -> kotlin.run {
                if (column.page) {
                    val paramStr = map[column.name]
                    val param = SU.toLong(paramStr)
                    if (param >= 0) {
                        when (column.type) {
                            Column.INT -> {
                                if (param > 0)
                                    sql += "AND `${column.name}` = $param "
                            }
                            Column.LONG -> {
                                if (param > 0)
                                    sql += "AND `${column.name}` = $param "
                            }
                            Column.BOOLEAN -> {
                                if (param == 0L)
                                    sql += "AND `${column.name}` = FALSE "
                                else if (param == 1L)
                                    sql += "AND `${column.name}` = TRUE "
                            }
                            Column.STRING -> {
                                if (SU.isNotEmpty(paramStr))
                                    sql += "AND `${column.name}` = '$paramStr'"
                            }
                            else -> throw IllegalArgumentException("search database error: not support column type while search page data.")
                        }
                    }
                }
            } }
            return "SELECT ${dataShow(table)} FROM ${table.name} WHERE `delete` = FALSE $sql ORDER BY `${table.order}` LIMIT ${if (page < 1) 0 else page.dec().times(row)},$row"
        }
    }

    /**
     * 数据保存SQL拼接
     * 参见SQL前缀拼接 dataSavePre() 和SQL后缀拼接 dataSaveEnd()
     */
    override fun dataSave(obj: JsonObject, table: DataTable) : String {
        return dataSavePre(table,true) + dataSaveEnd(obj, table)
    }

    /**
     * 保存多个数据的SQL语句组装
     * 保存步骤为：
     * dataSavePre() + dataSaveEnd() + ',' + dataSaveEnd() +...
     */
    override fun dataSave(array: JsonArray, table: DataTable) : String {
        if (array.isEmpty)
            throw IllegalArgumentException("invalid body data.")
        val pre = dataSavePre(table,false)
        var end = ""
        val size = array.size()
        for (i in 0 until size) {
            end += dataSaveEnd(array.getJsonObject(i),table)
            if (i != size.dec())
                end += ","
        }
        return pre + end
    }

    /**
     * 组装保存数据SQL语句的前面部分
     * INSERT INTO table_name(column1,column2,column3) VALUE
     * INSERT INTO table_name(column1,column2,column3) VALUES
     * single字段为true时为VALUE，false时为VALUES
     * @param table 表结构数据
     * @param single 是否为单条数据
     */
    private fun dataSavePre(table: DataTable, single: Boolean) : String {
        var sql = "INSERT INTO ${table.name}("
        table.columns.forEach { column -> kotlin.run {
            if (column.type != Column.IGNORE)
                sql += "`${column.name}`,"
        } }
        sql = sql.substring(0,sql.length.dec())
        sql += ")"
        sql += if (single) " VALUE " else " VALUES "
        return sql
    }

    /**
     * 组装保存sql语句的后面部分
     * 数据类型参数检查，数据类型Enum统一继承Table类
     * 对各个字段进行检查，有问题抛出异常，无问题拼接SQL并返回
     * 检查事项：
     *     数据body不能为空
     *     数据body字段不能多于表字段，防止恶意传递数据
     *     字符串字段不能为空
     *     字符串字段不能大于指定长度
     *     数字类型不能为空
     *     数字类型不能小于等于0
     *     布尔类型：如果字段为空，则按照table数据中len值赋值（0：false，1：true）
     *     文本字段目前没有作判断，对空或null值都进行保存。但再data_table.json中len字段做了KB大小限制
     */
    private fun dataSaveEnd(obj: JsonObject, table: DataTable) : String {
        if (obj.isEmpty || obj.size() > table.columns.size)
            throw IllegalArgumentException("invalid body data.")
        var sql = "("
        table.columns.forEach { column -> kotlin.run {
            val name = column.name
            when (column.type) {
                Column.LONG -> {
                    val value = obj.getLong(name) ?: throw IllegalArgumentException(notNull(name))
                    if (value <= 0)
                        throw IllegalArgumentException(invalid(name))
                    sql += "$value,"
                }
                Column.INT -> {
                    val value = obj.getInteger(name) ?: throw IllegalArgumentException(notNull(name))
                    if (value <= 0)
                        throw IllegalArgumentException(invalid(name))
                    sql += "$value,"
                }
                Column.BOOLEAN -> {
                    val value = obj.getBoolean(name)
                    sql += when {
                        null != value -> "$value,"
                        column.len == 0 -> "FALSE,"
                        else -> "TRUE,"
                    }
                }
                Column.STRING -> {
                    val value = obj.getString(name)
                    if (SU.isEmpty(value))
                        throw IllegalArgumentException(notNull(name))
                    if (value.length > column.len)
                        throw IllegalArgumentException(tooLong(name))
                    sql += "'$value',"
                }
                Column.TEXT -> {
                    val value = obj.getString(name)
                    sql += "'$value',"
                }
                Column.KEY -> {
                    val value = ApiServer.idBuilder.nextId()
                    sql += "$value,"
                }
                Column.IGNORE -> {}
                Column.UPDATE -> {
                    val value = System.currentTimeMillis()
                    sql += "$value,"
                }
            }
        } }
        return sql.substring(0,sql.length.dec())+")"
    }



    /**
     * 数据更新SQL拼接
     * 检查body数据是否不为空，为正常数据
     * 数据更新的表中（继承Table的Enum类）必须包含UPDATE字段和KEY字段类型，否则不予保存
     * 更新数据判断：
     * 数据body中没有该字段或该字段为null，则该字段不会被更新
     * 字段为空字符串时会被更新。（""）
     */
    override fun dataUpdate(obj: JsonObject, table: DataTable) : String {
        if (obj.isEmpty || obj.size() > table.columns.size)
            throw IllegalArgumentException("invalid body data.")
        val sqlPre = "UPDATE $table SET "
        var sqlCenter = ""
        var sqlEnd = ""
        var sqlUpdate = ""
        table.columns.forEach { column -> kotlin.run {
            val name = column.name
            when (column.type) {
                Column.LONG -> {
                    val value = obj.getLong(name)
                    if (null != value){
                        if (value < 0)
                            throw IllegalArgumentException(invalid(name))
                        sqlCenter += "`$name` = $value,"
                    }
                }
                Column.INT -> {
                    val value = obj.getInteger(name)
                    if (null != value) {
                        if (value < 0)
                            throw IllegalArgumentException(invalid(name))
                        sqlCenter += "`$name` = $value,"
                    }
                }
                Column.BOOLEAN -> {
                    val value = obj.getBoolean(name)
                    if (null != value) {
                        sqlCenter += "`$name` = $value,"
                    }
                }
                Column.STRING -> {
                    val value = obj.getString(name)
                    if (null != value) {
                        if (value.length > column.len)
                            throw IllegalArgumentException(tooLong(name))
                        sqlCenter += "`$name` = '$value',"
                    }
                }
                Column.TEXT -> {
                    val value = obj.getString(name)
                    if (null != value) {
                        sqlCenter += "`$name` = '$value',"
                    }
                }
                Column.KEY -> {
                    val value = obj.getLong(name) ?: throw IllegalArgumentException(notNull(name))
                    if (value <= 0)
                        throw IllegalArgumentException(invalid(name))
                    sqlEnd = "WHERE `$name` = $value"
                }
                Column.IGNORE -> {}
                Column.UPDATE -> {
                    val time = System.currentTimeMillis()
                    sqlUpdate = "`$name` = $time"
                }
            }
        } }
        if (sqlCenter.isEmpty())
            throw IllegalArgumentException("no column data to update.")
        if (sqlEnd.isEmpty())
            throw IllegalArgumentException("cannot find any key column to update.")
        if (sqlUpdate.isEmpty())
            throw IllegalArgumentException("cannot find any update column to update.")
        return sqlPre + sqlCenter + sqlUpdate + sqlEnd
    }

    /**
     * 获取数据分类数据
     * 数据分类数据都以数据类型分类，每个数据类型都有一个或多个分类类型，每个类型下面又多个类型值
     * @param type 数据分类类型
     */
    override fun dataDict(type: DataType) : String {
        return "SELECT * FROM tt_data_dict WHERE `data` = '${type.name}'"
    }

    private fun tooLong(name: String) : String = "param $name is too long."
    private fun notNull(name: String) : String = "param $name must not be null."
    private fun invalid(name: String) : String = "invalid param $name"

}