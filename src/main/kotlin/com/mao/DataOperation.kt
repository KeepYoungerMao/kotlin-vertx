package com.mao

import com.mao.Response.notAllowed
import com.mao.Response.ok
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface DataOperation : Handler<RoutingContext> {

    companion object {
        fun created() : DataOperation = DataOperationImpl()
    }

    override fun handle(ctx: RoutingContext)

}

class DataOperationImpl : DataOperation {

    private val queryHandler: Query = Query()

    override fun handle(ctx: RoutingContext) {
        val operation = ctx.pathParam("operation")
        val data = ctx.pathParam("data")
        val method = ctx.pathParam("method")
        val operationEnum = TypeOperation.operationType(operation)
        val dataEnum = TypeOperation.dataType(data)
        val methodEnum = TypeOperation.dataMethod(method)
        if (TypeOperation.canRequest(operationEnum,ctx.request().method())) {
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
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBook(ctx)
                                DataMethod.LIST -> saveBooks(ctx)
                                DataMethod.CHAPTER -> saveBookChapter(ctx)
                                DataMethod.CHAPTERS -> saveBookChapters(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBook(ctx)
                                DataMethod.LIST -> updateBooks(ctx)
                                DataMethod.CHAPTER -> updateBookChapter(ctx)
                                DataMethod.CHAPTERS -> updateBookChapters(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBook(ctx)
                                DataMethod.LIST -> deleteBooks(ctx)
                                DataMethod.CHAPTER -> deleteBookChapter(ctx)
                                DataMethod.CHAPTERS -> deleteBookChapters(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
                    }
                }
                DataType.BJX -> {
                    when (operationEnum) {
                        OperationType.SEARCH -> {
                            when (methodEnum) {
                                DataMethod.SRC -> bjxSrc(ctx)
                                DataMethod.LIST -> bjxList(ctx)
                                DataMethod.PAGE -> bjxPage(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBjx(ctx)
                                DataMethod.LIST -> saveBjxS(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBjx(ctx)
                                DataMethod.LIST -> updateBjxS(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBjx(ctx)
                                DataMethod.LIST -> deleteBjxS(ctx)
                                else -> ctx.response().end(notAllowed("not support data method $method"))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
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
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveBuddhist(ctx)
                                DataMethod.LIST -> saveBuddhists(ctx)
                                DataMethod.CHAPTER -> saveBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> saveBuddhistChapters(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateBuddhist(ctx)
                                DataMethod.LIST -> updateBuddhists(ctx)
                                DataMethod.CHAPTER -> updateBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> updateBuddhistChapters(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteBuddhist(ctx)
                                DataMethod.LIST -> deleteBuddhists(ctx)
                                DataMethod.CHAPTER -> deleteBuddhistChapter(ctx)
                                DataMethod.CHAPTERS -> deleteBuddhistChapters(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
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
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveLive(ctx)
                                DataMethod.LIST -> saveLives(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateLive(ctx)
                                DataMethod.LIST -> updateLives(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteLive(ctx)
                                DataMethod.LIST -> deleteLives(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
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
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> saveMovie(ctx)
                                DataMethod.LIST -> saveMovies(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updateMovie(ctx)
                                DataMethod.LIST -> updateMovies(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deleteMovie(ctx)
                                DataMethod.LIST -> deleteMovies(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
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
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.SAVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> savePic(ctx)
                                DataMethod.LIST -> savePics(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.EDIT -> {
                            when (methodEnum) {
                                DataMethod.SRC -> updatePic(ctx)
                                DataMethod.LIST -> updatePics(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        OperationType.REMOVE -> {
                            when (methodEnum) {
                                DataMethod.SRC -> deletePic(ctx)
                                DataMethod.LIST -> deletePics(ctx)
                                else -> ctx.response().end(notAllowed("request method not allowed."))
                            }
                        }
                        else -> ctx.response().end(notAllowed("request method not allowed."))
                    }
                }
                else -> ctx.response().end(notAllowed("not support data type $data"))
            }
        } else {
            ctx.response().end(notAllowed("request method not allowed."))
        }
    }

    private fun bookSrc(ctx: RoutingContext) {
        val id = ctx.request().getParam("id")?.toLong()?:-1
        if (id <= 0)
            ctx.response().end(Response.error("invalid param id."))
        else {
            val query = "SELECT * FROM tt_book2 WHERE id = $id"
            queryHandler.execute(query, single = true, commit = false, handler = Handler { res -> kotlin.run {
                if (res.succeeded()) {
                    ctx.response().end(res.result())
                } else {
                    ctx.response().end(Response.error("query database error."))
                }
            } })
        }
    }
    private fun bookList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bookPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bookChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bookChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bookClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBook(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBooks(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBookChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBookChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBook(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBooks(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBookChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBookChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBook(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBooks(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBookChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBookChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    private fun bjxSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bjxList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun bjxPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBjx(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBjxS(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBjx(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBjxS(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBjx(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBjxS(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    private fun buddhistSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun buddhistList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun buddhistPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun buddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun buddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun buddhistClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhist(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhists(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveBuddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhist(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhists(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateBuddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBuddhist(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBuddhists(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBuddhistChapter(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteBuddhistChapters(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    private fun liveSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun liveList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun livePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun liveClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveLive(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveLives(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateLive(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateLives(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteLive(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteLives(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    private fun movieSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun movieList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun moviePage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun movieClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveMovie(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun saveMovies(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateMovie(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updateMovies(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteMovie(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deleteMovies(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

    private fun picSrc(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picList(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picPage(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun picClassify(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun savePic(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun savePics(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updatePic(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun updatePics(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deletePic(ctx: RoutingContext) { ctx.response().end(ok("building...")) }
    private fun deletePics(ctx: RoutingContext) { ctx.response().end(ok("building...")) }

}