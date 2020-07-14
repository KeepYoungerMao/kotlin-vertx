package com.mao.service

import com.mao.server.ApiServer
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class RootService : Handler<RoutingContext>, BaseService() {

    companion object {
        const val FAVICON = "/favicon.ico"
    }

    override fun handle(ctx: RoutingContext) {
        if (ctx.request().path() == FAVICON)
            ctx.response().end()
        else {
            ctx.response().putHeader("content-type","application/json charset=utf-8")
            if (ApiServer.server.authorize)
                verify(ctx.request().getHeader(AUTHORIZATION))
            ctx.next()
        }
    }

}