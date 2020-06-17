package com.mao.service

import com.mao.enum.DataType
import com.mao.util.SU
import io.vertx.core.MultiMap

/**
 * 操作数据库的SQL组建类
 * 每一个方法都是固定方法，没有适用性。
 * 【注】：参数为字符串时需添加：''
 */
class SqlBuilder {

    fun dataDict(type: DataType) : String {
        return "SELECT * FROM tt_data_dict WHERE `data` = '${type.name}'"
    }

    fun bookSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_book WHERE id = $id"
    }

    fun bookList(map: MultiMap) : String? {
        val name = map["name"]
        val auth = map["auth"]
        val hasName = SU.isNotEmpty(name)
        val hasAuth = SU.isNotEmpty(auth)
        if (!hasName && !hasAuth)
            return null
        val row = SU.toLong(map["row"],20)
        var sql = """
            SELECT `id`,`name`,`auth`,`s_image`,`guide`,`guide_auth`,`score`,`type`,`type_id`,
            `dynasty`,`dynasty_id`,`count`,`free`,`off_sale` FROM tt_book WHERE `delete` = FALSE 
        """
        if (hasName)
            sql += "AND LOCATE('$name',`name`) > 0 "
        if (hasAuth)
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
        var sql = """
            SELECT `id`,`name`,`auth`,`s_image`,`guide`,`guide_auth`,`score`,`type`,`type_id`,
            `dynasty`,`dynasty_id`,`count`,`free`,`off_sale` FROM tt_book WHERE `delete` = FALSE 
        """
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

    fun bookChapter(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_book_chapter WHERE id = $id"
    }

    fun bookChapters(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT `id`,`order`,`name`,`book_id` FROM tt_book_chapter WHERE book_id = $id ORDER BY `order`"
    }

    fun bjxSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_bjx WHERE id = $id"
    }

    fun bjxList(map: MultiMap) : String? {
        val name = map["map"]
        val row = SU.toLong(map["row"],20)
        return if (SU.isEmpty(name))
            null
        else
            "SELECT `id`,`name`,`py` FROM tt_bjx WHERE `delete` = FALSE AND LOCATE('$name',`name`) > 0 LIMIT $row"
    }

    fun bjxPage(map: MultiMap) : String {
        val page = SU.toLong(map["page"],0)
        val row = SU.toLong(map["row"],20)
        val py = map["py"]
        var sql = "SELECT `id`,`name`,`py` FROM tt_bjx WHERE `delete` = FALSE "
        if (SU.isNotEmpty(py))
            sql += "AND `py` = '$py' "
        sql += "LIMIT ${if (page < 1) 0 else page.dec().times(row)},$row"
        return sql
    }

    fun buddhistSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_buddhist WHERE id = $id"
    }

    fun buddhistList(map: MultiMap) : String? {
        val row = SU.toLong(map["row"],20)
        val auth = map["auth"]
        val name = map["name"]
        val hasName = SU.isNotEmpty(name)
        val hasAuth = SU.isNotEmpty(auth)
        if (!hasName && !hasAuth)
            return null
        var sql = """
            SELECT `id`,`name`,`auth`,`image`,`type`,`type_id`,`update`,`delete` 
            FROM tt_buddhist WHERE `delete` = FALSE 
        """
        if (hasName)
            sql += "AND LOCATE('$name',`name`) > 0 "
        if (hasAuth)
            sql += "AND LOCATE('$auth',`auth`) > 0 "
        sql += "LIMIT $row"
        return sql
    }

    fun buddhistPage(map: MultiMap) : String {
        val page = SU.toLong(map["page"],0)
        val row = SU.toLong(map["row"],20)
        val type = SU.toLong(map["type"])
        var sql = """
            SELECT `id`,`name`,`auth`,`image`,`type`,`type_id`,`update`,`delete` 
            FROM tt_buddhist WHERE `delete` = FALSE 
        """
        if (type > 0)
            sql += "AND `type_id` = $type "
        sql += "LIMIT ${if (page < 1) 0 else page.dec().times(row)},$row"
        return sql
    }

    fun buddhistChapter(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_buddhist_chapter WHERE id = $id"
    }

    fun buddhistChapters(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT `id`,`order`,`pid`,`title` FROM tt_buddhist_chapter WHERE pid = $id ORDER BY `order`"
    }

}