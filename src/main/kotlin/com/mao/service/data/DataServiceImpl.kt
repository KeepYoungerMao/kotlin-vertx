package com.mao.service.data

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class DataServiceImpl : DataService, DataRootService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        val method = ctx.request().method()
        when(ctx.pathParam(DATA)) {
            DATA_BJX -> {
                when (method) {
                    HttpMethod.GET -> {
                        when(type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.bjxSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.bjxList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.bjxPage(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when (type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updateBjx(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when (type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.saveBjx(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.saveBjxS(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when (type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            DATA_BOOK -> {
                when (method) {
                    HttpMethod.GET -> {
                        when(type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.bookSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.bookList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.bookPage(it) }
                            CHAPTER_TYPE -> output(ctx) { sqlBuilder.bookChapter(it) }
                            CHAPTERS_TYPE -> children(ctx) { sqlBuilder.bookChapters(it) }
                            CLASSIFY_TYPE -> dict(ctx,DataTableEnum.BOOK.name)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updateBook(it) }
                            CHAPTER_TYPE -> input(ctx) { sqlBuilder.updateBookChapter(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.saveBook(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.saveBooks(it) }
                            CHAPTER_TYPE -> input(ctx) { sqlBuilder.saveBookChapter(it) }
                            CHAPTERS_TYPE -> inputs(ctx) { sqlBuilder.saveBookChapters(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when(type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            CHAPTER_TYPE -> refuse(ctx, NO_OPEN)
                            CHAPTERS_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            DATA_BUDDHIST -> {
                when (method) {
                    HttpMethod.GET -> {
                        when (type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.buddhistSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.buddhistList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.buddhistPage(it) }
                            CHAPTER_TYPE -> output(ctx) { sqlBuilder.buddhistChapter(it) }
                            CHAPTERS_TYPE -> children(ctx) { sqlBuilder.buddhistChapters(it) }
                            CLASSIFY_TYPE -> dict(ctx,DataTableEnum.BUDDHIST.name)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when (type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updateBuddhist(it) }
                            CHAPTER_TYPE -> input(ctx) { sqlBuilder.updateBuddhistChapter(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when (type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.saveBuddhist(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.saveBuddhists(it) }
                            CHAPTER_TYPE -> input(ctx) { sqlBuilder.saveBuddhistChapter(it) }
                            CHAPTERS_TYPE -> inputs(ctx) { sqlBuilder.saveBuddhistChapters(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when (type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            CHAPTER_TYPE -> refuse(ctx, NO_OPEN)
                            CHAPTERS_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            DATA_LIVE -> {
                when (method) {
                    HttpMethod.GET -> {
                        when(type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.liveSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.liveList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.livePage(it) }
                            CLASSIFY_TYPE -> dict(ctx,DataTableEnum.LIVE.name)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updateLive(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.saveLive(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.saveLives(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when(type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            DATA_MOVIE -> {
                when (method) {
                    HttpMethod.GET -> {
                        when(type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.movieSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.movieList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.moviePage(it) }
                            CLASSIFY_TYPE -> dict(ctx,DataTableEnum.MOVIE.name)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updateMovie(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.saveMovie(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.saveMovies(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when(type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            DATA_PIC -> {
                when (method) {
                    HttpMethod.GET -> {
                        when(type) {
                            SRC_TYPE -> output(ctx) { sqlBuilder.picSrc(it) }
                            LIST_TYPE -> outputs(ctx) { sqlBuilder.picList(it) }
                            PAGE_TYPE -> outputs(ctx) { sqlBuilder.picPage(it) }
                            CLASSIFY_TYPE -> dict(ctx,DataTableEnum.PIC.name)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.POST -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.updatePic(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.PUT -> {
                        when(type) {
                            SRC_TYPE -> input(ctx) { sqlBuilder.savePic(it) }
                            LIST_TYPE -> inputs(ctx) { sqlBuilder.savePics(it) }
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    HttpMethod.DELETE -> {
                        when(type) {
                            SRC_TYPE -> refuse(ctx, NO_OPEN)
                            LIST_TYPE -> refuse(ctx, NO_OPEN)
                            else -> refuse(ctx, TYPE_ERR)
                        }
                    }
                    else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
                }
            }
            else -> refuse(ctx, DATA_TYPE_ERR)
        }
    }

}