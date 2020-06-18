package com.mao.service

import com.mao.sql.Query
import com.mao.data.Response
import com.mao.enum.DataMethod
import com.mao.enum.DataType
import com.mao.enum.OperationType
import com.mao.enum.EnumOperation
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.lang.Exception

interface DataOperation : Handler<RoutingContext> {

    companion object {
        fun created() : DataOperation = DataOperationImpl()
    }

}

class DataOperationImpl : DataOperation {

    private val queryHandler: Query = Query()
    private val sqlBuilder: SqlBuilder = SqlBuilder()

    override fun handle(ctx: RoutingContext) {
        val operation = ctx.pathParam("operation")
        val data = ctx.pathParam("data")
        val method = ctx.pathParam("method")
        val operationEnum = EnumOperation.operationType(operation)
        val dataEnum = EnumOperation.dataType(data)
        val methodEnum = EnumOperation.dataMethod(method)
        if (EnumOperation.canRequest(operationEnum,ctx.request().method())) {
            when (dataEnum) {
                DataType.BOOK -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> bookSrc(ctx)
                                DataMethod.LIST -> bookList(ctx)
                                DataMethod.PAGE -> bookPage(ctx)
                                DataMethod.CHAPTER -> bookChapter(ctx)
                                DataMethod.CHAPTERS -> bookChapters(ctx)
                                DataMethod.CLASSIFY -> bookClassify(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBook(ctx)
                                DataMethod.LIST -> saveBooks(ctx)
                                DataMethod.CHAPTER -> saveBookChapter(ctx)
                                DataMethod.CHAPTERS -> saveBookChapters(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBook(ctx)
                                DataMethod.CHAPTER -> updateBookChapter(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBook(ctx)
                                DataMethod.LIST -> deleteBooks(ctx)
                                DataMethod.CHAPTER -> deleteBookChapter(ctx)
                                DataMethod.CHAPTERS -> deleteBookChapters(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                DataType.BJX -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> bjxSrc(ctx)
                                DataMethod.LIST -> bjxList(ctx)
                                DataMethod.PAGE -> bjxPage(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBjx(ctx)
                                DataMethod.LIST -> saveBjxS(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBjx(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBjx(ctx)
                                DataMethod.LIST -> deleteBjxS(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                DataType.BUDDHIST -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> buddhistSrc(ctx)
                                DataMethod.LIST -> buddhistList(ctx)
                                DataMethod.PAGE -> buddhistPage(ctx)
                                DataMethod.CHAPTER -> buddhistChapter(ctx)
                                DataMethod.CHAPTERS -> buddhistChapters(ctx)
                                DataMethod.CLASSIFY -> buddhistClassify(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBuddhist(ctx)
                                DataMethod.LIST -> saveBuddhists(ctx)
                                DataMethod.CHAPTER -> saveBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> saveBuddhistChapters(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBuddhist(ctx)
                                DataMethod.CHAPTER -> updateBuddhistChapter(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBuddhist(ctx)
                                DataMethod.LIST -> deleteBuddhists(ctx)
                                DataMethod.CHAPTER -> deleteBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> deleteBuddhistChapters(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                DataType.LIVE -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> liveSrc(ctx)
                                DataMethod.LIST -> liveList(ctx)
                                DataMethod.PAGE -> livePage(ctx)
                                DataMethod.CLASSIFY -> liveClassify(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveLive(ctx)
                                DataMethod.LIST -> saveLives(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateLive(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteLive(ctx)
                                DataMethod.LIST -> deleteLives(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                DataType.MOVIE -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> movieSrc(ctx)
                                DataMethod.LIST -> movieList(ctx)
                                DataMethod.PAGE -> moviePage(ctx)
                                DataMethod.CLASSIFY -> movieClassify(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveMovie(ctx)
                                DataMethod.LIST -> saveMovies(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateMovie(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteMovie(ctx)
                                DataMethod.LIST -> deleteMovies(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                DataType.PIC -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> picSrc(ctx)
                                DataMethod.LIST -> picList(ctx)
                                DataMethod.PAGE -> picPage(ctx)
                                DataMethod.CLASSIFY -> picClassify(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> savePic(ctx)
                                DataMethod.LIST -> savePics(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updatePic(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deletePic(ctx)
                                DataMethod.LIST -> deletePics(ctx)
                                else -> ctx.response().end(methodError(method))
                            }
                        }
                        else -> ctx.response().end(operationError(operation))
                    }
                }
                else -> ctx.response().end(dataError(data))
            }
        } else {
            ctx.response().end(requestError(ctx.request().method().name))
        }
    }

    /**
     * 通过古籍id查询古籍详情
     */
    private fun bookSrc(ctx: RoutingContext) {
        val sql = sqlBuilder.bookSrc(ctx.request().getParam("id"))
        if (sql == null)
            ctx.response().end(error("invalid param id."))
        else {
            sqlResult(ctx, sql, single = true, commit = false)
        }
    }

    /**
     * 查询古籍列表，搜索查询
     * 支持参数：
     * name：古籍名称
     * auth：古籍作者
     */
    private fun bookList(ctx: RoutingContext) {
        val sql = sqlBuilder.bookList(ctx.request().params())
        if (null == sql)
            ctx.response().end(ok(emptyList<Any>()))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 查询古籍列表，分类查询，分页查询
     * 支持参数：
     * page：页码
     * row：每页数量
     * type：估计类型
     * dynasty：古籍朝代
     * free：是否免费
     * off_sale：是否下架
     */
    private fun bookPage(ctx: RoutingContext) {
        val sql = sqlBuilder.bookPage(ctx.request().params())
        sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 通过id查询古籍章节详情
     * id错误返回错误信息
     */
    private fun bookChapter(ctx: RoutingContext) {
        val sql = sqlBuilder.bookChapter(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }

    /**
     * 通过古籍id查询该古籍下所有章节列表
     * 章节列表不包含章节详情内容
     * 古籍id错误返回错误信息
     */
    private fun bookChapters(ctx: RoutingContext) {
        val sql = sqlBuilder.bookChapters(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 返回古籍分类信息
     */
    private fun bookClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.BOOK),single = false,commit = false)
    }

    /**
     * 保存古籍数据
     * 接收body参数，使用JsonObject接收
     */
    private fun saveBook(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBook(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个古籍数据
     * 接收body参数，使用JsonArray接收
     */
    private fun saveBooks(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBooks(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 保存古籍章节数据
     * 接收body参数，使用JsonObject接收
     */
    private fun saveBookChapter(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBookChapter(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个古籍章节数据
     * 接收body参数，使用JsonArray接收
     */
    private fun saveBookChapters(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBookChapters(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 更新古籍数据
     * 接收body参数，使用JsonObject接收
     * 不支持多个数据同时更新
     */
    private fun updateBook(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateBook(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 更新古籍章节数据
     * 接收body参数，使用JsonObject接收
     * 不支持多个数据同时更新
     */
    private fun updateBookChapter(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateBookChapter(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除古籍数据，接收古籍id
     * 暂时不开放
     */
    private fun deleteBook(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete book data"))
    }

    /**
     * 删除多个古籍数据，接收古籍id拼接字符串
     * 暂时不开放
     */
    private fun deleteBooks(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete book data"))
    }

    /**
     * 删除古籍章节数据，接收章节id
     * 暂时不开放
     */
    private fun deleteBookChapter(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete book chapter data"))
    }

    /**
     * 删除多个古籍章节数据，接收删除模式
     * 支持根据古籍id删除，支持根据id拼接字符串删除
     * 暂时不开放
     */
    private fun deleteBookChapters(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete book chapter data"))
    }

    /**
     * 根据id查询百家姓详情
     * id错误返回错误信息
     */
    private fun bjxSrc(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxSrc(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }

    /**
     * 检索查询百家姓列表
     * 检索参数：
     * name：百家姓名称
     */
    private fun bjxList(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxList(ctx.request().params())
        if (null == sql)
            ctx.response().end(ok(emptyList<Any>()))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 分页查询百家姓列表
     * 分页参数：
     * page、row
     * py：百家姓首字母：A-Z
     */
    private fun bjxPage(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxPage(ctx.request().params())
        sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 保存百家姓数据
     * 接收body参数，使用JsonObject接收
     */
    private fun saveBjx(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBjx(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个百家姓数据
     * 接收body参数，使用JsonArray接收
     */
    private fun saveBjxS(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBjxS(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 更新百家姓数据
     * 接收body参数，使用JsonObject接收
     */
    private fun updateBjx(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateBjx(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除百家姓数据，接收百家姓id
     * 暂时不开放
     */
    private fun deleteBjx(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete bjx data"))
    }

    /**
     * 删除多个百家姓数据，接收百家姓id拼接字符串
     * 暂时不开放
     */
    private fun deleteBjxS(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete bjx data"))
    }

    /**
     * 根据id查询佛经详情
     * id错误返回错误信息
     */
    private fun buddhistSrc(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistSrc(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }

    /**
     * 检索佛经列表
     * 检索参数：
     * name：佛经名称
     * auth：佛经作者
     * 检索参数为空返回空集合
     */
    private fun buddhistList(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistList(ctx.request().params())
        if (null == sql)
            ctx.response().end(ok(emptyList<Any>()))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 分页查询佛经列表
     * 分页参数：
     * page、row
     * type：佛经类型
     */
    private fun buddhistPage(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistPage(ctx.request().params())
        sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 根据id查询佛经章节详情
     * id错误返回错误信息
     */
    private fun buddhistChapter(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistChapter(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }

    /**
     * 根据佛经id查询该佛经下多有章节列表
     * 章节简要列表
     * id错误返回错误信息
     */
    private fun buddhistChapters(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistChapters(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }

    /**
     * 返回佛经的分类数据
     */
    private fun buddhistClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.BUDDHIST),single = false,commit = false)
    }

    /**
     * 保存佛经数据
     */
    private fun saveBuddhist(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBuddhist(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个佛经数据
     */
    private fun saveBuddhists(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBuddhists(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 保存佛经章节数据
     */
    private fun saveBuddhistChapter(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBuddhistChapter(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个佛经章节数据
     */
    private fun saveBuddhistChapters(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveBuddhistChapters(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 更新佛经数据
     */
    private fun updateBuddhist(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateBuddhist(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 更新佛经章节数据
     */
    private fun updateBuddhistChapter(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateBuddhistChapter(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除佛经数据
     * 接收佛经id
     * 暂时不开放
     */
    private fun deleteBuddhist(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete buddhist data"))
    }

    /**
     * 删除多个佛经数据
     * 接收佛经id拼接字符串
     * 暂时不开放
     */
    private fun deleteBuddhists(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete buddhist data"))
    }

    /**
     * 删除佛经章节数据
     * 接收佛经章节id
     * 暂时不开放
     */
    private fun deleteBuddhistChapter(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete buddhist chapter data"))
    }

    /**
     * 删除多个佛经章节数据
     * 接收删除模式：接收佛经章节id拼接字符串 或 佛经id
     * 暂时不开放
     */
    private fun deleteBuddhistChapters(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete buddhist chapter data"))
    }

    private fun liveSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun liveList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun livePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    /**
     * 查询直播源分类数据
     */
    private fun liveClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.LIVE),single = false,commit = false)
    }

    /**
     * 保存直播源数据
     */
    private fun saveLive(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveLive(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个直播源数据
     */
    private fun saveLives(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveLives(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = false, commit = true)
    }

    /**
     * 更新直播源数据
     */
    private fun updateLive(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateLive(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除直播源数据，接收直播源id
     * 暂时不开放
     */
    private fun deleteLive(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete live data"))
    }

    /**
     * 删除多个直播源数据，接收id拼接字符串
     * 暂时不开放
     */
    private fun deleteLives(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete live data"))
    }

    private fun movieSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun movieList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun moviePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    /**
     * 查询电影分类数据
     */
    private fun movieClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.MOVIE),single = false,commit = false)
    }

    /**
     * 保存电影数据
     */
    private fun saveMovie(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveMovie(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个电影数据
     */
    private fun saveMovies(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.saveMovies(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 更新电影数据
     */
    private fun updateMovie(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updateMovie(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除电影数据，接收电影id参数
     * 暂时不开放
     */
    private fun deleteMovie(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete movie data"))
    }

    /**
     * 删除多个电影数据，接收id拼接字符串
     * 暂时不开放
     */
    private fun deleteMovies(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete movie data"))
    }

    private fun picSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    /**
     * 查询图片分类数据
     */
    private fun picClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.PIC),single = false,commit = false)
    }

    /**
     * 保存图片数据
     */
    private fun savePic(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.savePic(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 保存多个图片数据
     */
    private fun savePics(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.savePics(ctx.bodyAsJsonArray)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 更新图片数据
     */
    private fun updatePic(ctx: RoutingContext) {
        var sql: String? = null
        try {
            sql = sqlBuilder.updatePic(ctx.bodyAsJson)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql)
            sqlResult(ctx, sql, single = true, commit = true)
    }

    /**
     * 删除图片数据，接收图片id参数
     * 暂时不开放
     */
    private fun deletePic(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete pic data"))
    }

    /**
     * 删除多个图片数据，接收id拼接字符串
     * 暂时不开放
     */
    private fun deletePics(ctx: RoutingContext) {
        ctx.response().end(permission("no permission to delete pic data"))
    }

    //=================================== SQL execute ================================================

    /**
     * 统一执行sql语句，并操作RoutingContext返回数据
     * 执行sql语句也是同一调用execute方法
     * @param ctx RoutingContext上下文
     * @param sql 执行的SQL语句
     * @param single 在SQL语句为查询类语句时使用，为true时表示只查询一条记录，false表示查询多条记录
     * @param commit 是否需要提交，需要提交的表示为更新、保存、删除类型SQL语句
     */
    private fun sqlResult(ctx: RoutingContext, sql: String, single: Boolean, commit: Boolean) {
        queryHandler.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ctx.response().end(res.result())
            } else {
                ctx.response().end(error("query database error."))
            }
        } })
    }

    //========================= error data response ===============================================

    private fun operationError(operation: String) : String = err("p1",operation)
    private fun dataError(data: String) : String = err("p2",data)
    private fun methodError(method: String) : String = err("p3",method)
    private fun err(p: String, name: String) : String {
        return Response.notAllowed("request data not allowed. request path: /api/data/{p1}/{p2}/{p3}, the param[$p] which you send[$name] is not supported.")
    }
    private fun requestError(request: String) : String {
        return Response.notAllowed("request method[$request] not allowed,")
    }
    private fun <T> ok(data: T) : String = Response.ok(data)
    private fun error(msg: String) : String = Response.error(msg)
    private fun permission(msg: String) : String = Response.permission(msg)

}