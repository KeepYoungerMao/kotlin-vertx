package com.mao.service.data.bjx

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface BjxService : Handler<RoutingContext> {

    companion object {

        fun create() : BjxService = BjxServiceImpl()

    }

}