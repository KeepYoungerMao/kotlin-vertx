package com.mao.service

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface AuthHandler : Handler<RoutingContext> {
    companion object {
        fun create() : AuthHandler = AuthHandlerImpl()
    }
}

/**
 * oauth2认证授权简化版
 * client表示用户，没有user概念
 * auto_improve为true：没有用户授权确认页面
 * 缓存内容：
 * access_token : client_id & start_time
 */
class AuthHandlerImpl : AuthHandler {

    override fun handle(ctx: RoutingContext) {
        authentication(ctx)
    }





    /**
     * 请求拦截
     * favicon.ico不做记录
     * 请求需要头部添加 Authorization ：access_token
     */
    private fun authentication(ctx: RoutingContext) {
        /*val path = ctx.request().path()
        if (path == FAVICON) ctx.response().end()
        else {
            val authCode = ctx.request().getHeader(AUTHORIZATION)
            if (null == authCode) permission(ctx,"need Authorization")
            else {
                val clientId = cachedToken[authCode]
                if (null == clientId) permission(ctx,"invalid Authorization.")
                else {
                    println("user[$clientId] searching path[$path]")
                    ctx.response().putHeader("content-type","application/json charset=utf-8")
                    ctx.next()
                }
            }
        }*/
        ctx.response().putHeader("content-type","application/json charset=utf-8")
        ctx.next()
    }



}