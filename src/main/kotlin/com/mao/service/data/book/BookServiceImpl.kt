package com.mao.service.data.book

import com.mao.service.data.DataTableEnum
import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class BookServiceImpl : BookService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when(type) {
                    SRC_TYPE -> bookSrc(ctx)
                    LIST_TYPE -> bookList(ctx)
                    PAGE_TYPE -> bookPage(ctx)
                    CHAPTER_TYPE -> bookChapter(ctx)
                    CHAPTERS_TYPE -> bookChapters(ctx)
                    CLASSIFY_TYPE -> bookClassify(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when(type) {
                    SRC_TYPE -> updateBook(ctx)
                    CHAPTER_TYPE -> updateBookChapter(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when(type) {
                    SRC_TYPE -> saveBook(ctx)
                    LIST_TYPE -> saveBooks(ctx)
                    CHAPTER_TYPE -> saveBookChapter(ctx)
                    CHAPTERS_TYPE -> saveBookChapters(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when(type) {
                    SRC_TYPE -> deleteBook(ctx)
                    LIST_TYPE -> deleteBooks(ctx)
                    CHAPTER_TYPE -> deleteBookChapter(ctx)
                    CHAPTERS_TYPE -> deleteBookChapters(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //通过古籍id查询古籍详情
    private fun bookSrc(ctx: RoutingContext) {
        execute(ctx,single = true,commit = false,args = ctx.request().getParam("id")) { sqlBuilder.bookSrc(it) }
    }

    //查询古籍列表，搜索查询
    private fun bookList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bookList(it) }
    }

    //查询古籍列表，分类查询，分页查询
    private fun bookPage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bookPage(it) }
    }

    //通过id查询古籍章节详情
    private fun bookChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bookChapter(it) }
    }

    //通过古籍id查询该古籍下所有章节列表
    private fun bookChapters(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bookChapters(it) }
    }

    //返回古籍分类信息
    private fun bookClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataTableEnum.BOOK.name),single = false,commit = false)
    }

    //保存古籍数据
    private fun saveBook(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBook(it) }
    }

    //保存多个古籍数据
    private fun saveBooks(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBooks(it) }
    }

    //保存古籍章节数据
    private fun saveBookChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBookChapter(it) }
    }

    //保存多个古籍章节数据
    private fun saveBookChapters(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBookChapters(it) }
    }

    //更新古籍数据
    private fun updateBook(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBook(it) }
    }

    //更新古籍章节数据
    private fun updateBookChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBookChapter(it) }
    }

    //删除古籍数据，接收古籍id
    private fun deleteBook(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个古籍数据，接收古籍id拼接字符串
    private fun deleteBooks(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除古籍章节数据，接收章节id
    private fun deleteBookChapter(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个古籍章节数据，接收删除模式
    private fun deleteBookChapters(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}