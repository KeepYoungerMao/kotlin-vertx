package com.mao

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface ErrorHandler : Handler<RoutingContext> {

    companion object {
        fun created(code: Int) : ErrorHandler = ErrorHandlerImpl(code)
    }

}

class ErrorHandlerImpl(private val code: Int) : ErrorHandler {

    override fun handle(ctx: RoutingContext) {
        ctx.response().end(
            when(code) {
                404 -> Response.notfound("no resource path: ${ctx.request().path()}")
                405 -> Response.notAllowed("request not allowed")
                500 -> Response.error("request error")
                else -> Response.error("")
            }
        )
    }

}