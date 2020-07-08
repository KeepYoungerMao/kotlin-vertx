package com.mao.service.data.movie

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface MovieService : Handler<RoutingContext> {

    companion object {
        fun create() : MovieService = MovieServiceImpl()
    }

}