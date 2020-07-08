package com.mao.service.data.pic

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface PicService : Handler<RoutingContext> {

    companion object {
        fun create() : PicService = PicServiceImpl()
    }

}