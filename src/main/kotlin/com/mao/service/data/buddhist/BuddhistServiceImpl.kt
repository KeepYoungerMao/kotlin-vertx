package com.mao.service.data.buddhist

import com.mao.service.data.DataTableEnum
import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class BuddhistServiceImpl : BuddhistService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when (type) {
                    SRC_TYPE -> buddhistSrc(ctx)
                    LIST_TYPE -> buddhistList(ctx)
                    PAGE_TYPE -> buddhistPage(ctx)
                    CHAPTER_TYPE -> buddhistChapter(ctx)
                    CHAPTERS_TYPE -> buddhistChapters(ctx)
                    CLASSIFY_TYPE -> buddhistClassify(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when (type) {
                    SRC_TYPE -> updateBuddhist(ctx)
                    CHAPTER_TYPE -> updateBuddhistChapter(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when (type) {
                    SRC_TYPE -> saveBuddhist(ctx)
                    LIST_TYPE -> saveBuddhists(ctx)
                    CHAPTER_TYPE -> saveBuddhistChapter(ctx)
                    CHAPTERS_TYPE -> saveBuddhistChapters(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when (type) {
                    SRC_TYPE -> deleteBuddhist(ctx)
                    LIST_TYPE -> deleteBuddhists(ctx)
                    CHAPTER_TYPE -> deleteBuddhistChapter(ctx)
                    CHAPTERS_TYPE -> deleteBuddhistChapters(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //根据id查询佛经详情
    private fun buddhistSrc(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistSrc(it) }
    }

    //检索佛经列表
    private fun buddhistList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.buddhistList(it) }
    }

    //分页查询佛经列表
    private fun buddhistPage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.buddhistPage(it) }
    }

    //根据id查询佛经章节详情
    private fun buddhistChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistChapter(it) }
    }

    //根据佛经id查询该佛经下多有章节列表
    private fun buddhistChapters(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.buddhistChapters(it) }
    }

    //返回佛经的分类数据
    private fun buddhistClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataTableEnum.BUDDHIST.name),single = false,commit = false)
    }

    //保存佛经数据
    private fun saveBuddhist(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBuddhist(it) }
    }

    //保存多个佛经数据
    private fun saveBuddhists(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBuddhists(it) }
    }

    //保存佛经章节数据
    private fun saveBuddhistChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBuddhistChapter(it) }
    }

    //保存多个佛经章节数据
    private fun saveBuddhistChapters(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBuddhistChapters(it) }
    }

    //更新佛经数据
    private fun updateBuddhist(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBuddhist(it) }
    }

    //更新佛经章节数据
    private fun updateBuddhistChapter(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBuddhistChapter(it) }
    }

    //删除佛经数据
    private fun deleteBuddhist(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个佛经数据
    private fun deleteBuddhists(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除佛经章节数据
    private fun deleteBuddhistChapter(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个佛经章节数据
    private fun deleteBuddhistChapters(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}