package com.mao.data

import com.mao.handler.ApiHandler

/**
 * 表字段数据类型，非通用型数据类型
 * 只是为了字段的检查设计使用
 */
enum class Column{LONG,INT,BOOLEAN,STRING,TEXT,KEY,IGNORE,UPDATE}

/**
 * 表数据分类
 */
enum class DataTableEnum{BOOK,BOOK_CHAPTER,BUDDHIST,BUDDHIST_CHAPTER,BJX}

/**
 * 表数据
 */
data class DataTable(val table: DataTableEnum, val name: String, val columns: MutableList<DataColumn>)

/**
 * 表字段数据
 */
data class DataColumn(val name: String, val type: Column, val len: Int)

/**
 * 获取表工具类
 */
object DataTableUtil {

    fun getTable(table: DataTableEnum) : DataTable? {
        ApiHandler.dataTable.forEach { if (it.table == table) return it }
        return null
    }

}