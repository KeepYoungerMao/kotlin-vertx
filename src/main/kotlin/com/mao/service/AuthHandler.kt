package com.mao.service

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface AuthHandler : Handler<RoutingContext> {
    companion object {
        fun create() : AuthHandler = AuthHandlerImpl()
    }
}

class AuthHandlerImpl : AuthHandler {

    companion object {
        const val Authorization = "Authorization"
    }

    override fun handle(ctx: RoutingContext) {
        val authCode = ctx.request().getHeader(Authorization)
        if (null == authCode)
            println("request without Authorization code.")
        else
            println("request with Authorization code: $authCode")
        ctx.response().putHeader("content-type","application/json charset=utf-8")
        ctx.next()
    }

}