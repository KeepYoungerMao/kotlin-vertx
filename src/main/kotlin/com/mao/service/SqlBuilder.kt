package com.mao.service

import com.mao.enum.DataType
import com.mao.util.SU
import io.vertx.core.MultiMap
import io.vertx.core.json.JsonObject

/**
 * 操作数据库的SQL组建类
 * 每一个方法都是固定方法，没有适用性。
 * 【注】：参数为字符串时需添加：''
 */
class SqlBuilder {

    /**
     * 获取数据分类数据
     * 数据分类数据都以数据类型分类，每个数据类型都有一个或多个分类类型，每个类型下面又多个类型值
     * @param type 数据分类类型
     */
    fun dataDict(type: DataType) : String {
        return "SELECT * FROM tt_data_dict WHERE `data` = '${type.name}'"
    }

    /**
     * 古籍详情SQL
     * 若参数id为空或不为数字类型，返回null
     */
    fun bookSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_book WHERE id = $id"
    }

    /**
     * 查询古籍列表SQL
     * 用于古籍数据的检索，检索参数支持：古籍名称【name】、古籍作者【auth】
     * 可额外传递条数限制参数row，默认最多20条数据
     * 古籍列表查询不返回大图片和古籍介绍
     * 检索必须包含一种参数，否则返回null
     */
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

    /**
     * 分页查询古籍列表SQL
     * 分页参数固定为：页码【page】、条数【row】
     * 分页查询支持分类参数：古籍类型【type】、古籍朝代【dynasty】、是否免费【free】、是否下架【off_sale】
     * 分页查询的分类参数只支持数字类型分类
     * 古籍分页查询不返回大图片和古籍介绍
     */
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

    /**
     * 查询古籍章节详情SQL
     * 根据章节id查询详情，返回全部数据
     * 参数id为空或不是数字类型返回null
     */
    fun bookChapter(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_book_chapter WHERE id = $id"
    }

    /**
     * 查询古籍所有章节列表SQL
     * 根据古籍id查询章节列表，章节列表不返回章节内容详情
     * 参数id为空或不是数字类型返回null
     */
    fun bookChapters(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT `id`,`order`,`name`,`book_id` FROM tt_book_chapter WHERE book_id = $id ORDER BY `order`"
    }

    /**
     * 保存古籍数据
     * id：              主键
     * name：            名称
     * auth：            作者
     * s_image：         小图片地址
     * image：           大图片地址
     * intro：           介绍
     * guide：           导读
     * guide_auth：      导读人
     * score：           得分
     * type：            古籍类型
     * type_id：         古籍类型id
     * dynasty：         朝代
     * dynasty_id：      朝代id
     * count：           数量
     * free：            是否免费
     * off_sale：        是否下架
     * update：          更新时间
     * delete：          是否删除
     */
    fun saveBook(obj: JsonObject) : String? {
        val name = obj.getString("name")
        val auth = obj.getString("auth")
        val sImage = obj.getString("s_image")
        val image = obj.getString("image")
        val intro = obj.getString("intro")
        val guide = obj.getString("guide")
        val guideAuth = obj.getString("guide_auth")
        val score = obj.getString("score")
        val type = obj.getString("type")
        val typeId = obj.getInteger("type_id")
        val dynasty = obj.getString("dynasty")
        val dynastyId = obj.getInteger("dynasty_id")
        val count = obj.getInteger("count")
        val free = obj.getBoolean("free")
        val offSale = obj.getBoolean("off_sale")
        return "SELECT save_book('$name','$auth','$sImage','$image','$intro','$guide','$guideAuth','$score','$type',$typeId,$dynasty,'$dynastyId',$count,$free,$offSale)"
    }

    /**
     * 查询百家姓详情SQL
     * 根据百家姓id查询详情数据，返回所有字段
     * 参数id为空或不是数字类型返回null
     */
    fun bjxSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_bjx WHERE id = $id"
    }

    /**
     * 查询百家姓列表SQL
     * 百家姓数据的检索，支持检索参数：百家姓名称【name】
     * 可携带条数限制参数【row】，默认最多返回20条数据
     * 无检索参数时返回null
     * 分页查询时不返回百家姓详情字段
     */
    fun bjxList(map: MultiMap) : String? {
        val name = map["map"]
        val row = SU.toLong(map["row"],20)
        return if (SU.isEmpty(name))
            null
        else
            "SELECT `id`,`name`,`py` FROM tt_bjx WHERE `delete` = FALSE AND LOCATE('$name',`name`) > 0 LIMIT $row"
    }

    /**
     * 分页查询百家姓列表SQL
     * 支持分类参数：拼音首字母【py】
     * 分页查询的分类参数只支持数字类型分类
     * 分页查询时不返回百家姓详情字段
     */
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

    /**
     * 查询佛经详情SQL
     * 根据佛经id查询，参数id为空或不为数字类型返回null
     */
    fun buddhistSrc(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_buddhist WHERE id = $id"
    }

    /**
     * 检索佛经列表SQL
     * 佛经数据的检索，支持检索参数：佛经名称【name】、佛经作者【auth】
     * 检索参数为空时返回null
     * 返回佛经列表不包含佛经介绍
     */
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

    /**
     * 分页查询佛经列表SQL
     * 支持分页参数：佛经类型【type】
     * 返回佛经列表不包含佛经介绍
     */
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

    /**
     * 查询佛经章节详情SQL
     * 参数id为空或不为数字时返回null
     */
    fun buddhistChapter(p: String?) : String? {
        val id: Long = try {
            p?.toLong()?:-1
        } catch (e: Exception) { -1 }
        return if (id <= 0)
            null
        else
            "SELECT * FROM tt_buddhist_chapter WHERE id = $id"
    }

    /**
     * 查询所有佛经章节列表SQL
     * 根据佛经id查询所有佛经章节列表
     * 返回列表不包含佛经详情字段
     */
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