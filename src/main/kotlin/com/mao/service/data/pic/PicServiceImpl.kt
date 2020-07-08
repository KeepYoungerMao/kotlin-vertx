package com.mao.service.data.pic

import com.mao.service.data.DataTableEnum
import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class PicServiceImpl : PicService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when(type) {
                    SRC_TYPE -> picSrc(ctx)
                    LIST_TYPE -> picList(ctx)
                    PAGE_TYPE -> picPage(ctx)
                    CLASSIFY_TYPE -> picClassify(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when(type) {
                    SRC_TYPE -> updatePic(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when(type) {
                    SRC_TYPE -> savePic(ctx)
                    LIST_TYPE -> savePics(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when(type) {
                    SRC_TYPE -> deletePic(ctx)
                    LIST_TYPE -> deletePics(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //查询图片详情
    private fun picSrc(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.picSrc(it) }
    }

    //检索图片列表
    private fun picList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.picList(it) }
    }

    //分页查询图片列表
    private fun picPage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.picPage(it) }
    }

    //查询图片分类数据
    private fun picClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataTableEnum.PIC.name),single = false,commit = false)
    }

    //保存图片数据
    private fun savePic(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.savePic(it) }
    }

    //保存多个图片数据
    private fun savePics(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.savePics(it) }
    }

    //更新图片数据
    private fun updatePic(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updatePic(it) }
    }

    //删除图片数据，接收图片id参数
    private fun deletePic(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个图片数据，接收id拼接字符串
    private fun deletePics(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}