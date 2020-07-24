package com.mao.service.data

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface TableService : Handler<RoutingContext> {

    companion object {
        fun create() : TableService = TableServiceImpl()
    }

}