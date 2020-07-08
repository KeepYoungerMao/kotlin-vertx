package com.mao.service.data.live

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface LiveService : Handler<RoutingContext> {

    companion object {
        fun create() : LiveService = LiveServiceImpl()
    }

}