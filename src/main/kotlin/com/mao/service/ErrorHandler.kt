package com.mao.service

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface ErrorHandler : Handler<RoutingContext> {

    companion object {
        fun created(code: Int) : ErrorHandler = ErrorHandlerImpl(code)
    }

}

/**
 * 错误返回处理
 * 代码中使用到的try catch,如：
 * try {
 *     ...
 *     ctx.response().end("ok")
 * } catch (e: Exception) {
 *     ctx.response().end("error")
 * }
 * 这种情况下，我们对错误进行了处理，但是使用router.errorHandler()依然会收到错误并进行处理
 * 此处判断response是否结束处理，若没有结束处理，则在此处进行错误数据返回。
 */
class ErrorHandlerImpl(private val code: Int) : ErrorHandler, BaseService() {

    override fun handle(ctx: RoutingContext) {
        if (!ctx.response().ended()) {
            ctx.failure()?.printStackTrace()
            when (code) {
                404 -> no(ctx,"no resource path: ${ctx.request().path()}")
                405 -> refuse(ctx,"request not allowed")
                500 -> err(ctx,"request error")
                else -> err(ctx,"request error")
            }
        }
    }

}