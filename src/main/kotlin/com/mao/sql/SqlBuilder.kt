package com.mao.sql

import com.mao.data.*
import com.mao.data.DataTableUtil.getTable
import com.mao.type.DataType
import io.vertx.core.MultiMap
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

/**
 * 操作数据库的SQL组建类
 * 每一个方法都是固定方法，没有适用性。
 * 【注】：参数为字符串时需添加：''
 */
abstract class SqlBuilder {

    //根据id（表主键、或次级表对应的主表的主键（如tt_book_chapter的父id：book_id））查询，list表示是否为多条数据
    abstract fun dataSearch(p: String?, table: DataTable, list: Boolean) : String
    //检索查询列表或分页查询列表，list=true表示检索查询
    abstract fun dataSearch(map: MultiMap, table: DataTable, list: Boolean) : String
    //单条数据保存
    abstract fun dataSave(obj: JsonObject, table: DataTable) : String
    //多条数据保存
    abstract fun dataSave(array: JsonArray, table: DataTable) : String
    //数据更新
    abstract fun dataUpdate(obj: JsonObject, table: DataTable) : String
    //字典数据
    abstract fun dataDict(type: DataType) : String


    //古籍详情SQL
    fun bookSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BOOK), false)

    //查询古籍列表SQL
    fun bookList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BOOK), true)

    //分页查询古籍列表SQL
    fun bookPage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BOOK), false)

    //查询古籍章节详情SQL
    fun bookChapter(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BOOK_CHAPTER), false)

    //查询古籍所有章节列表SQL
    fun bookChapters(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BOOK_CHAPTER), true)

    //保存古籍数据SQL
    fun saveBook(obj: JsonObject) : String = dataSave(obj,getTable(DataTableEnum.BOOK))

    //保存多个古籍数据SQL
    fun saveBooks(array: JsonArray) : String = dataSave(array,getTable(DataTableEnum.BOOK))

    //保存古籍章节详情数据SQL
    fun saveBookChapter(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.BOOK_CHAPTER))

    //保存多个古籍章节详情SQL
    fun saveBookChapters(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.BOOK_CHAPTER))

    //更新古籍数据
    fun updateBook(obj: JsonObject) : String = dataUpdate(obj,getTable(DataTableEnum.BOOK))

    //更新古籍章节数据
    fun updateBookChapter(obj: JsonObject) : String = dataUpdate(obj,getTable(DataTableEnum.BOOK_CHAPTER))

    //查询百家姓详情SQL
    fun bjxSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BJX), false)

    //查询百家姓列表SQL
    fun bjxList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BJX), true)

    //分页查询百家姓列表SQL
    fun bjxPage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BJX), false)

    //保存百家姓数据SQL
    fun saveBjx(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.BJX))

    //保存多个百家姓数据SQL
    fun saveBjxS(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.BJX))

    //更新百家姓数据SQL
    fun updateBjx(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.BJX))

    //查询佛经详情SQL
    fun buddhistSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BUDDHIST), false)

    //检索佛经列表SQL
    fun buddhistList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BUDDHIST), true)

    //分页查询佛经列表SQL
    fun buddhistPage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.BUDDHIST), false)

    //查询佛经章节详情SQL
    fun buddhistChapter(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BUDDHIST_CHAPTER), false)

    //查询所有佛经章节列表SQL
    fun buddhistChapters(p: String?) : String = dataSearch(p, getTable(DataTableEnum.BUDDHIST_CHAPTER), true)

    //保存佛经数据SQL
    fun saveBuddhist(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.BUDDHIST))

    //保存多个佛经数据SQL
    fun saveBuddhists(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.BUDDHIST))

    //保存佛经章节数据SQL
    fun saveBuddhistChapter(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.BUDDHIST_CHAPTER))

    //保存多个佛经章节数据SQL
    fun saveBuddhistChapters(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.BUDDHIST_CHAPTER))

    //更新佛经数据SQL
    fun updateBuddhist(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.BUDDHIST))

    //更新佛经章节数据SQL
    fun updateBuddhistChapter(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.BUDDHIST_CHAPTER))

    //查询直播源详情
    fun liveSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.LIVE), false)

    //检索直播源数据，关键词搜索
    fun liveList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.LIVE), true)

    //分页查询直播源数据
    fun livePage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.LIVE), false)

    //保存直播源数据
    fun saveLive(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.LIVE))

    //保存多个直播源数据
    fun saveLives(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.LIVE))

    //更新直播源数据
    fun updateLive(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.LIVE))

    //查询电影详情
    fun movieSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.MOVIE), false)

    //检索电影数据，关键词搜索
    fun movieList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.MOVIE), true)

    //分页查询电影数据
    fun moviePage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.MOVIE), false)

    //保存电影数据
    fun saveMovie(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.MOVIE))

    //保存多个电影数据
    fun saveMovies(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.MOVIE))

    //更新电影数据
    fun updateMovie(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.MOVIE))

    //查询图片详情
    fun picSrc(p: String?) : String = dataSearch(p, getTable(DataTableEnum.PIC), false)

    //检索图片数据，关键词搜索
    fun picList(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.PIC), true)

    //分页查询图片数据
    fun picPage(map: MultiMap) : String = dataSearch(map, getTable(DataTableEnum.PIC), false)

    //保存图片数据
    fun savePic(obj: JsonObject) : String = dataSave(obj, getTable(DataTableEnum.PIC))

    //保存多个图片数据
    fun savePics(array: JsonArray) : String = dataSave(array, getTable(DataTableEnum.PIC))

    //更新图片数据
    fun updatePic(obj: JsonObject) : String = dataUpdate(obj, getTable(DataTableEnum.PIC))

}