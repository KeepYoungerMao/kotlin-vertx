package com.mao.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mao.data.ResEnum
import com.mao.data.ResponseData
import io.vertx.ext.web.RoutingContext

open class BaseService {

    companion object {
        const val TYPE_ERR = "type error. no this type of resource data."
    }

    fun ok(ctx: RoutingContext, data: Any) {
        ctx.response().end(json(data, ResEnum.OK))
    }

    protected fun refuse(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg, ResEnum.NOTALLOWED))
    }

    protected fun err(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg, ResEnum.ERROR))
    }

    protected fun auth(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg, ResEnum.PERMISSION))
    }

    protected fun no(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg,ResEnum.NOTFOUND))
    }

    /**
     * ResponseData转化为json
     * 内联方法
     */
    private inline fun <reified T> json(data: T, type: ResEnum) : String {
        return jacksonObjectMapper().writeValueAsString(ResponseData(type.code, type.msg, data))
    }

}