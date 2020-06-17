package com.mao.service

import com.mao.sql.Query
import com.mao.data.Response
import com.mao.enum.DataMethod
import com.mao.enum.DataType
import com.mao.enum.OperationType
import com.mao.enum.EnumOperation
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

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
                                DataMethod.LIST -> updateBooks(ctx)
                                DataMethod.CHAPTER -> updateBookChapter(ctx)
                                DataMethod.CHAPTERS -> updateBookChapters(ctx)
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
                                DataMethod.LIST -> updateBjxS(ctx)
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
                                DataMethod.LIST -> updateBuddhists(ctx)
                                DataMethod.CHAPTER -> updateBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> updateBuddhistChapters(ctx)
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
                                DataMethod.LIST -> updateLives(ctx)
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
                                DataMethod.LIST -> updateMovies(ctx)
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
                                DataMethod.LIST -> updatePics(ctx)
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
    private fun saveBook(ctx: RoutingContext) {
        println(ctx.bodyAsJson)
        ctx.response().end(permission("on building"))
    }
    private fun saveBooks(ctx: RoutingContext) {
        println(ctx.bodyAsJsonArray)
        ctx.response().end(permission("on building"))
    }
    private fun saveBookChapter(ctx: RoutingContext) {
        println(ctx.bodyAsJson)
        ctx.response().end(permission("on building"))
    }
    private fun saveBookChapters(ctx: RoutingContext) {
        println(ctx.bodyAsJsonArray)
        ctx.response().end(permission("on building"))
    }
    private fun updateBook(ctx: RoutingContext) {
        println(ctx.bodyAsJson)
        ctx.response().end(permission("on building"))
    }
    private fun updateBooks(ctx: RoutingContext) {
        println(ctx.bodyAsJsonArray)
        ctx.response().end(permission("on building"))
    }
    private fun updateBookChapter(ctx: RoutingContext) {
        println(ctx.bodyAsJson)
        ctx.response().end(permission("on building"))
    }
    private fun updateBookChapters(ctx: RoutingContext) {
        println(ctx.bodyAsJsonArray)
        ctx.response().end(permission("on building"))
    }
    private fun deleteBook(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBooks(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBookChapter(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBookChapters(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    private fun bjxSrc(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxSrc(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }
    private fun bjxList(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxList(ctx.request().params())
        if (null == sql)
            ctx.response().end(ok(emptyList<Any>()))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }
    private fun bjxPage(ctx: RoutingContext) {
        val sql = sqlBuilder.bjxPage(ctx.request().params())
        sqlResult(ctx, sql, single = false, commit = false)
    }
    private fun saveBjx(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBjxS(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBjx(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBjxS(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBjx(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBjxS(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    private fun buddhistSrc(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistSrc(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }
    private fun buddhistList(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistList(ctx.request().params())
        if (null == sql)
            ctx.response().end(ok(emptyList<Any>()))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }
    private fun buddhistPage(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistPage(ctx.request().params())
        sqlResult(ctx, sql, single = false, commit = false)
    }
    private fun buddhistChapter(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistChapter(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = true, commit = false)
    }
    private fun buddhistChapters(ctx: RoutingContext) {
        val sql = sqlBuilder.buddhistChapters(ctx.request().getParam("id"))
        if (null == sql)
            ctx.response().end(error("invalid param id."))
        else
            sqlResult(ctx, sql, single = false, commit = false)
    }
    private fun buddhistClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataType.BUDDHIST),single = false,commit = false)
    }
    private fun saveBuddhist(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhists(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhist(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhists(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBuddhist(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBuddhists(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBuddhistChapter(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteBuddhistChapters(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    private fun liveSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun liveList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun livePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun liveClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveLive(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveLives(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateLive(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateLives(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteLive(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteLives(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    private fun movieSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun movieList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun moviePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun movieClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveMovie(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveMovies(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateMovie(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateMovies(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteMovie(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deleteMovies(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    private fun picSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun savePic(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun savePics(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updatePic(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updatePics(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deletePic(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }
    private fun deletePics(ctx: RoutingContext) {
        //TODO 暂未开放
        ctx.response().end(permission("no permission to delete data"))
    }

    /**
     * 同一执行sql语句，并操作RoutingContext返回数据
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