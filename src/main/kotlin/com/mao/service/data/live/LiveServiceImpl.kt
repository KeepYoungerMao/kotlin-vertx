package com.mao.service.data.live

import com.mao.service.data.DataTableEnum
import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class LiveServiceImpl : LiveService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when(type) {
                    SRC_TYPE -> liveSrc(ctx)
                    LIST_TYPE -> liveList(ctx)
                    PAGE_TYPE -> livePage(ctx)
                    CLASSIFY_TYPE -> liveClassify(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when(type) {
                    SRC_TYPE -> updateLive(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when(type) {
                    SRC_TYPE -> saveLive(ctx)
                    LIST_TYPE -> saveLives(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when(type) {
                    SRC_TYPE -> deleteLive(ctx)
                    LIST_TYPE -> deleteLives(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //查询直播源详情
    private fun liveSrc(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.liveSrc(it)}
    }

    //检索直播源列表
    private fun liveList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.liveList(it) }
    }

    //分页查询直播源数据
    private fun livePage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.livePage(it) }
    }

    //查询直播源分类数据
    private fun liveClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataTableEnum.LIVE.name),single = false,commit = false)
    }

    //保存直播源数据
    private fun saveLive(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveLive(it) }
    }

    //保存多个直播源数据
    private fun saveLives(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveLives(it) }
    }

    //更新直播源数据
    private fun updateLive(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateLive(it) }
    }

    //删除直播源数据，接收直播源id
    private fun deleteLive(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个直播源数据，接收id拼接字符串
    private fun deleteLives(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}