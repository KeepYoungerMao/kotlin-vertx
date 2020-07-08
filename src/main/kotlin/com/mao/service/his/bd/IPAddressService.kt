package com.mao.service.his.bd

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface IPAddressService : Handler<RoutingContext> {

    companion object {
        fun create() : IPAddressService = IpAddressServiceImpl()
    }

}