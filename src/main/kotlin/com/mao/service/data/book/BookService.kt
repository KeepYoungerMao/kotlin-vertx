package com.mao.service.data.book

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface BookService : Handler<RoutingContext> {

    companion object {
        fun create() : BookService = BookServiceImpl()
    }

}