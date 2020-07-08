package com.mao.service.data

import com.mao.server.ApiServer

/**
 * 表字段数据类型，非通用型数据类型
 * 只是为了字段的检查设计使用
 */
enum class Column{LONG,INT,BOOLEAN,STRING,TEXT,KEY,IGNORE,UPDATE}

/**
 * 表数据分类
 */
enum class DataTableEnum{BOOK,BOOK_CHAPTER,BUDDHIST,BUDDHIST_CHAPTER,BJX,LIVE,MOVIE,PIC}

/**
 * 表数据
 * @param table 表类型
 * @param name 表名称
 * @param main 是否是顶级表：这个字段只要是区别 book - book_chapter 这样的上下表关系
 * @param order 排序字段名称
 * @param columns 表下字段数据
 */
data class DataTable(val table: DataTableEnum, val name: String, val main: Boolean, val order: String, val columns: MutableList<DataColumn>)

/**
 * 表字段数据
 * 用于查询、保存、更新的参数设置
 * 默认查询详情时全表数据都展现
 * 默认查询详情时使用type=KEY进行查询，默认KEY字段为Long类型字段
 * 检索查询和分页查询列表时展现show=true的字段
 * 保存，更新时更具type字段和len字段进行判断
 * @param name 字段名称
 * @param type 字段类型
 * @param len 字段允许长度，STRING、INT、LONG、KEY类型表示长度，TEXT表示KB值，BOOLEAN表示0：false，1：true
 * @param list 是否为检索查询的查询字段
 * @param page 是否为分页查询的查询字段
 * @param show 列表查询时字段是否展示
 */
data class DataColumn(val name: String, val type: Column, val len: Int, val list: Boolean, val page: Boolean, val show: Boolean)

/**
 * 获取表工具类
 */
object DataTableUtil {

    fun getTable(table: DataTableEnum) : DataTable {
        ApiServer.dataTable.forEach { if (it.table == table) return it }
        throw IllegalArgumentException("database error: not found this table")
    }

}