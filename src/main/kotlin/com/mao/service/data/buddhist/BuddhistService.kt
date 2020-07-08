package com.mao.service.data.buddhist

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface BuddhistService : Handler<RoutingContext> {

    companion object {
        fun create() : BuddhistService = BuddhistServiceImpl()
    }

}