package com.mao.service.auth

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface AuthService : Handler<RoutingContext> {

    companion object {
        fun create() : AuthService = AuthServiceImpl()
    }

}