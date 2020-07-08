package com.mao.service.data.bjx

import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class BjxServiceImpl : BjxService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when(type) {
                    SRC_TYPE -> bjxSrc(ctx)
                    LIST_TYPE -> bjxList(ctx)
                    PAGE_TYPE -> bjxPage(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when (type) {
                    SRC_TYPE -> updateBjx(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when (type) {
                    SRC_TYPE -> saveBjx(ctx)
                    LIST_TYPE -> saveBjxS(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when (type) {
                    SRC_TYPE -> deleteBjx(ctx)
                    LIST_TYPE -> deleteBjxS(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //根据id查询百家姓详情
    private fun bjxSrc(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.bjxSrc(it) }
    }

    //检索查询百家姓列表
    private fun bjxList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bjxList(it) }
    }

    //分页查询百家姓列表
    private fun bjxPage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.bjxPage(it) }
    }

    //保存百家姓数据
    private fun saveBjx(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveBjx(it) }
    }

    //保存多个百家姓数据
    private fun saveBjxS(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveBjxS(it) }
    }

    //更新百家姓数据
    private fun updateBjx(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateBjx(it) }
    }

    //删除百家姓数据，接收百家姓id
    private fun deleteBjx(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个百家姓数据，接收百家姓id拼接字符串
    private fun deleteBjxS(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}