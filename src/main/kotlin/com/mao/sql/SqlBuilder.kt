package com.mao.sql

import com.mao.data.*
import com.mao.enum.DataType
import io.vertx.core.MultiMap
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

/**
 * 操作数据库的SQL组建类
 * 每一个方法都是固定方法，没有适用性。
 * 【注】：参数为字符串时需添加：''
 */
abstract class SqlBuilder {

    abstract fun dataSearch(p: String?, table: DataTable, list: Boolean) : String
    abstract fun dataSearch(map: MultiMap, table: DataTable, list: Boolean) : String
    abstract fun dataSave(obj: JsonObject, table: DataTable) : String
    abstract fun dataSave(array: JsonArray, table: DataTable) : String
    abstract fun dataUpdate(obj: JsonObject, table: DataTable) : String
    abstract fun dataDict(type: DataType) : String

    /**
     * 古籍详情SQL
     * 若参数id为空或不为数字类型，返回null
     */
    fun bookSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataSearch(p, table, false)
    }

    /**
     * 查询古籍列表SQL
     * 用于古籍数据的检索，检索参数支持：古籍名称【name】、古籍作者【auth】
     * 可额外传递条数限制参数row，默认最多20条数据
     * 古籍列表查询不返回大图片和古籍介绍
     * 检索必须包含一种参数，否则返回null
     */
    fun bookList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询古籍列表SQL
     * 分页参数固定为：页码【page】、条数【row】
     * 分页查询支持分类参数：古籍类型【type】、古籍朝代【dynasty】、是否免费【free】、是否下架【off_sale】
     * 分页查询的分类参数只支持数字类型分类
     * 古籍分页查询不返回大图片和古籍介绍
     */
    fun bookPage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataSearch(map, table, false)
    }

    /**
     * 查询古籍章节详情SQL
     * 根据章节id查询详情，返回全部数据
     * 参数id为空或不是数字类型返回null
     */
    fun bookChapter(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK_CHAPTER)
        return dataSearch(p, table, false)
    }

    /**
     * 查询古籍所有章节列表SQL
     * 根据古籍id查询章节列表，章节列表不返回章节内容详情
     * 参数id为空或不是数字类型返回null
     */
    fun bookChapters(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK_CHAPTER)
        return dataSearch(p, table, true)
    }

    /**
     * 保存古籍数据SQL
     * 数据各项进行检查，检查错误抛出异常，由调用方法拦截
     */
    fun saveBook(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataSave(obj,table)
    }

    /**
     * 保存多个古籍数据SQL
     * 遍历多个古籍数据，并依次进行检查，只要有一个错误便抛出异常，所有数据都不会保存
     * 异常由调用方法进行拦截
     */
    fun saveBooks(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataSave(array,table)
    }

    /**
     * 保存古籍章节详情数据SQL
     */
    fun saveBookChapter(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK_CHAPTER)
        return dataSave(obj, table)
    }

    /**
     * 保存多个古籍章节详情SQL
     */
    fun saveBookChapters(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK_CHAPTER)
        return dataSave(array, table)
    }

    /**
     * 更新古籍数据
     */
    fun updateBook(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK)
        return dataUpdate(obj,table)
    }

    /**
     * 更新古籍章节数据
     */
    fun updateBookChapter(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BOOK_CHAPTER)
        return dataUpdate(obj,table)
    }

    /**
     * 查询百家姓详情SQL
     * 根据百家姓id查询详情数据，返回所有字段
     * 参数id为空或不是数字类型返回null
     */
    fun bjxSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataSearch(p, table, false)
    }

    /**
     * 查询百家姓列表SQL
     * 百家姓数据的检索，支持检索参数：百家姓名称【name】
     * 可携带条数限制参数【row】，默认最多返回20条数据
     * 无检索参数时返回null
     * 分页查询时不返回百家姓详情字段
     */
    fun bjxList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询百家姓列表SQL
     * 支持分类参数：拼音首字母【py】
     * 分页查询的分类参数只支持数字类型分类
     * 分页查询时不返回百家姓详情字段
     */
    fun bjxPage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataSearch(map, table, false)
    }

    /**
     * 保存百家姓数据SQL
     */
    fun saveBjx(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataSave(obj, table)
    }

    /**
     * 保存多个百家姓数据SQL
     */
    fun saveBjxS(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataSave(array, table)
    }

    /**
     * 更新百家姓数据SQL
     */
    fun updateBjx(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BJX)
        return dataUpdate(obj, table)
    }

    /**
     * 查询佛经详情SQL
     * 根据佛经id查询，参数id为空或不为数字类型返回null
     */
    fun buddhistSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataSearch(p, table, false)
    }

    /**
     * 检索佛经列表SQL
     * 佛经数据的检索，支持检索参数：佛经名称【name】、佛经作者【auth】
     * 检索参数为空时返回null
     * 返回佛经列表不包含佛经介绍
     */
    fun buddhistList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询佛经列表SQL
     * 支持分页参数：佛经类型【type】
     * 返回佛经列表不包含佛经介绍
     */
    fun buddhistPage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataSearch(map, table, false)
    }

    /**
     * 查询佛经章节详情SQL
     * 参数id为空或不为数字时返回null
     */
    fun buddhistChapter(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST_CHAPTER)
        return dataSearch(p, table, false)
    }

    /**
     * 查询所有佛经章节列表SQL
     * 根据佛经id查询所有佛经章节列表
     * 返回列表不包含佛经详情字段
     */
    fun buddhistChapters(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST_CHAPTER)
        return dataSearch(p, table, true)
    }

    /**
     * 保存章节数据SQL
     */
    fun saveBuddhist(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataSave(obj, table)
    }

    /**
     * 保存多个章节数据SQL
     */
    fun saveBuddhists(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataSave(array, table)
    }

    /**
     * 保存佛经章节数据SQL
     */
    fun saveBuddhistChapter(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST_CHAPTER)
        return dataSave(obj, table)
    }

    /**
     * 保存多个佛经章节数据SQL
     */
    fun saveBuddhistChapters(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST_CHAPTER)
        return dataSave(array, table)
    }

    /**
     * 更新佛经数据SQL
     */
    fun updateBuddhist(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST)
        return dataUpdate(obj, table)
    }

    /**
     * 更新佛经章节数据SQL
     */
    fun updateBuddhistChapter(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.BUDDHIST_CHAPTER)
        return dataUpdate(obj, table)
    }

    /**
     * 查询直播源详情
     */
    fun liveSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataSearch(p, table, false)
    }

    /**
     * 检索直播源数据，关键词搜索
     */
    fun liveList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询直播源数据
     */
    fun livePage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataSearch(map, table, false)
    }

    /**
     * 保存直播源数据
     */
    fun saveLive(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataSave(obj, table)
    }

    /**
     * 保存多个直播源数据
     */
    fun saveLives(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataSave(array, table)
    }

    /**
     * 更新直播源数据
     */
    fun updateLive(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.LIVE)
        return dataUpdate(obj, table)
    }

    /**
     * 查询电影详情
     */
    fun movieSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataSearch(p, table, false)
    }

    /**
     * 检索电影数据，关键词搜索
     */
    fun movieList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询电影数据
     */
    fun moviePage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataSearch(map, table, false)
    }

    /**
     * 保存电影数据
     */
    fun saveMovie(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataSave(obj, table)
    }

    /**
     * 保存多个电影数据
     */
    fun saveMovies(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataSave(array, table)
    }

    /**
     * 更新电影数据
     */
    fun updateMovie(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.MOVIE)
        return dataUpdate(obj, table)
    }

    /**
     * 查询图片详情
     */
    fun picSrc(p: String?) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataSearch(p, table, false)
    }

    /**
     * 检索图片数据，关键词搜索
     */
    fun picList(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataSearch(map, table, true)
    }

    /**
     * 分页查询图片数据
     */
    fun picPage(map: MultiMap) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataSearch(map, table, false)
    }

    /**
     * 保存图片数据
     */
    fun savePic(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataSave(obj, table)
    }

    /**
     * 保存多个图片数据
     */
    fun savePics(array: JsonArray) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataSave(array, table)
    }

    /**
     * 更新图片数据
     */
    fun updatePic(obj: JsonObject) : String {
        val table = DataTableUtil.getTable(DataTableEnum.PIC)
        return dataUpdate(obj, table)
    }

}