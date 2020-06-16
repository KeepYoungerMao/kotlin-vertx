package com.mao.service

import com.mao.util.SU
import io.vertx.core.MultiMap

/**
 * 操作数据库的SQL组建类
 * 每一个方法都是固定方法，没有适用性。
 */
class SqlBuilder {

    fun bookSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_book WHERE id = $id"
    }

    fun bookList(map: MultiMap) : String {
        val name = map["name"]
        val auth = map["auth"]
        val row = SU.toLong(map["row"],20)
        var sql = "SELECT * FROM tt_book WHERE `delete` = FALSE "
        if (SU.isNotEmpty(name))
            sql += "AND LOCATE('$name',`name`) > 0 "
        if (SU.isNotEmpty(auth))
            sql += "AND LOCATE('$auth',`auth`) > 0 "
        sql += "LIMIT $row"
        return sql
    }

    fun bookPage(map: MultiMap) : String {
        val page = SU.toLong(map["page"],0)
        val row = SU.toLong(map["row"],20)
        val type = SU.toLong(map["type"])
        val dynasty = SU.toLong(map["dynasty"])
        val free = SU.toLong(map["free"])
        val offSale = SU.toLong(map["off_sale"])
        var sql = "SELECT * FROM tt_book WHERE `delete` = FALSE "
        if (type > 0)
            sql += "AND `type_id` = $type "
        if (dynasty > 0)
            sql += "AND `dynasty_id` = $dynasty "
        if (free == 0L)
            sql += "AND `free` = FALSE "
        else if (free == 1L)
            sql += "AND `free` = TRUE "
        if (offSale == 0L)
            sql += "AND `off_sale` = FALSE "
        else if (offSale == 1L)
            sql += "AND `off_sale` = TRUE "
        sql += "LIMIT ${if (page < 1) 0 else page.dec().times(row)},$row"
        return sql
    }

}