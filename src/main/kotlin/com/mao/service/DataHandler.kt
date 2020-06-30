package com.mao.service

import com.mao.data.DataTableEnum
import com.mao.sql.Query
import com.mao.data.Response
import com.mao.type.DataMethod
import com.mao.type.DataType
import com.mao.type.OperationType
import com.mao.type.Operation
import com.mao.sql.SqlBuilder
import com.mao.sql.SqlBuilderExecute
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.lang.Exception

interface DataHandler : Handler<RoutingContext> {

    companion object {
        fun created() : DataHandler = DataHandlerImpl()
    }

}

class DataHandlerImpl : DataHandler {

    private val query: Query = Query.create()
    private val sqlBuilder: SqlBuilder = SqlBuilderExecute()

    override fun handle(ctx: RoutingContext) {
        val operation = ctx.pathParam("operation")
        val data = ctx.pathParam("data")
        val method = ctx.pathParam("method")
        val operationEnum = Operation.operationType(operation)
        val dataEnum = Operation.dataType(data)
        val methodEnum = Operation.dataMethod(method)
        if (Operation.canRequest(operationEnum,ctx.request().method())) {
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

    //通过古籍id查询古籍详情
    private fun bookSrc(ctx: RoutingContext) {
        dataExecute(ctx,single = true,commit = false,args = ctx.request().getParam("id")) { sqlBuilder.bookSrc(it) }
    }

    //查询古籍列表，搜索查询
    private fun bookList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bookList(it) }
    }

    //查询古籍列表，分类查询，分页查询
    private fun bookPage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bookPage(it) }
    }

    //通过id查询古籍章节详情
    private fun bookChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bookChapter(it) }
    }

    //通过古籍id查询该古籍下所有章节列表
    private fun bookChapters(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bookChapters(it) }
    }

    //返回古籍分类信息
    private fun bookClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.BOOK),single = false,commit = false)
    }

    //保存古籍数据
    private fun saveBook(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBook(it) }
    }

    //保存多个古籍数据
    private fun saveBooks(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBooks(it) }
    }

    //保存古籍章节数据
    private fun saveBookChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBookChapter(it) }
    }

    //保存多个古籍章节数据
    private fun saveBookChapters(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBookChapters(it) }
    }

    //更新古籍数据
    private fun updateBook(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBook(it) }
    }

    //更新古籍章节数据
    private fun updateBookChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBookChapter(it) }
    }

    //删除古籍数据，接收古籍id
    private fun deleteBook(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BOOK)
    }

    //删除多个古籍数据，接收古籍id拼接字符串
    private fun deleteBooks(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BOOK)
    }

    //删除古籍章节数据，接收章节id
    private fun deleteBookChapter(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BOOK_CHAPTER)
    }

    //删除多个古籍章节数据，接收删除模式
    private fun deleteBookChapters(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BOOK_CHAPTER)
    }

    //根据id查询百家姓详情
    private fun bjxSrc(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bjxSrc(it) }
    }

    //检索查询百家姓列表
    private fun bjxList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bjxList(it) }
    }

    //分页查询百家姓列表
    private fun bjxPage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bjxPage(it) }
    }

    //保存百家姓数据
    private fun saveBjx(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBjx(it) }
    }

    //保存多个百家姓数据
    private fun saveBjxS(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBjxS(it) }
    }

    //更新百家姓数据
    private fun updateBjx(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBjx(it) }
    }

    //删除百家姓数据，接收百家姓id
    private fun deleteBjx(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BJX)
    }

    //删除多个百家姓数据，接收百家姓id拼接字符串
    private fun deleteBjxS(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BJX)
    }

    //根据id查询佛经详情
    private fun buddhistSrc(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistSrc(it) }
    }

    //检索佛经列表
    private fun buddhistList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.buddhistList(it) }
    }

    //分页查询佛经列表
    private fun buddhistPage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.buddhistPage(it) }
    }

    //根据id查询佛经章节详情
    private fun buddhistChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistChapter(it) }
    }

    //根据佛经id查询该佛经下多有章节列表
    private fun buddhistChapters(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistChapters(it) }
    }

    //返回佛经的分类数据
    private fun buddhistClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.BUDDHIST),single = false,commit = false)
    }

    //保存佛经数据
    private fun saveBuddhist(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBuddhist(it) }
    }

    //保存多个佛经数据
    private fun saveBuddhists(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBuddhists(it) }
    }

    //保存佛经章节数据
    private fun saveBuddhistChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBuddhistChapter(it) }
    }

    //保存多个佛经章节数据
    private fun saveBuddhistChapters(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBuddhistChapters(it) }
    }

    //更新佛经数据
    private fun updateBuddhist(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBuddhist(it) }
    }

    //更新佛经章节数据
    private fun updateBuddhistChapter(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBuddhistChapter(it) }
    }

    //删除佛经数据
    private fun deleteBuddhist(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BUDDHIST)
    }

    //删除多个佛经数据
    private fun deleteBuddhists(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BUDDHIST)
    }

    //删除佛经章节数据
    private fun deleteBuddhistChapter(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BUDDHIST_CHAPTER)
    }

    //删除多个佛经章节数据
    private fun deleteBuddhistChapters(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.BUDDHIST_CHAPTER)
    }

    //查询直播源详情
    private fun liveSrc(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.liveSrc(it)}
    }

    //检索直播源列表
    private fun liveList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.liveList(it) }
    }

    //分页查询直播源数据
    private fun livePage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.livePage(it) }
    }

    //查询直播源分类数据
    private fun liveClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.LIVE),single = false,commit = false)
    }

    //保存直播源数据
    private fun saveLive(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveLive(it) }
    }

    //保存多个直播源数据
    private fun saveLives(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveLives(it) }
    }

    //更新直播源数据
    private fun updateLive(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateLive(it) }
    }

    //删除直播源数据，接收直播源id
    private fun deleteLive(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.LIVE)
    }

    //删除多个直播源数据，接收id拼接字符串
    private fun deleteLives(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.LIVE)
    }

    //查询电影详情
    private fun movieSrc(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.movieSrc(it) }
    }

    //检索电影列表
    private fun movieList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.movieList(it) }
    }

    //分页查询电影列表
    private fun moviePage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.moviePage(it) }
    }

    //查询电影分类数据
    private fun movieClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.MOVIE),single = false,commit = false)
    }

    //保存电影数据
    private fun saveMovie(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveMovie(it) }
    }

    //保存多个电影数据
    private fun saveMovies(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveMovies(it) }
    }

    //更新电影数据
    private fun updateMovie(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateMovie(it) }
    }

    //删除电影数据，接收电影id参数
    private fun deleteMovie(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.MOVIE)
    }

    //删除多个电影数据，接收id拼接字符串
    private fun deleteMovies(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.MOVIE)
    }

    //查询图片详情
    private fun picSrc(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.picSrc(it) }
    }

    //检索图片列表
    private fun picList(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.picList(it) }
    }

    //分页查询图片列表
    private fun picPage(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.picPage(it) }
    }

    //查询图片分类数据
    private fun picClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.PIC),single = false,commit = false)
    }

    //保存图片数据
    private fun savePic(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.savePic(it) }
    }

    //保存多个图片数据
    private fun savePics(ctx: RoutingContext) {
        dataExecute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.savePics(it) }
    }

    //更新图片数据
    private fun updatePic(ctx: RoutingContext) {
        dataExecute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updatePic(it) }
    }

    //删除图片数据，接收图片id参数
    private fun deletePic(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.PIC)
    }

    //删除多个图片数据，接收id拼接字符串
    private fun deletePics(ctx: RoutingContext) {
        noOpen(ctx,DataTableEnum.PIC)
    }

    //=================================== 方法统一执行 ================================================

    /**
     * 数据请求、数据操作统一方法
     */
    private fun <T> dataExecute(ctx: RoutingContext,
                            single: Boolean, commit: Boolean,
                            args: T, sqlMethod: (arg: T) -> String) {
        var sql: String? = null
        try {
            sql = sqlMethod(args)
        } catch (e: Exception) {
            ctx.response().end(error(e.message?:"request error"))
        }
        if (null != sql )
            sqlResult(ctx, sql, single, commit)
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
        query.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ctx.response().end(res.result())
            } else {
                ctx.response().end(error("query database error."))
            }
        } })
    }

    //========================= error data response ===============================================

    private fun noOpen(ctx: RoutingContext, data: DataTableEnum) {
        ctx.response().end(permission("no permission to delete ${data.name.toLowerCase()} data"))
    }

    private fun operationError(operation: String) : String = err("p1",operation)
    private fun dataError(data: String) : String = err("p2",data)
    private fun methodError(method: String) : String = err("p3",method)
    private fun err(p: String, name: String) : String {
        return Response.notAllowed("request data not allowed. request path: /api/data/{p1}/{p2}/{p3}, the param[$p] which you send[$name] is not supported.")
    }
    private fun requestError(request: String) : String {
        return Response.notAllowed("request method[$request] not allowed,")
    }
    private fun error(msg: String) : String = Response.error(msg)
    private fun permission(msg: String) : String = Response.permission(msg)

}