package com.mao.service.his.bd

import com.mao.server.ApiServer
import com.mao.service.BaseService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class IpAddressServiceImpl : IPAddressService, BaseService() {

    companion object {
        const val AK = "Po86Y8fZwYv5fpcQIX7MVk1DaMOl3VwB"
    }

    override fun handle(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.GET)
            refuse(ctx,"request method ${ctx.request().method().name} not allowed")
        else {
            val ip = ctx.request().getParam("ip")
            if (null == ip)
                err(ctx,"loss param ip")
            else {
                ApiServer.webClient.getAbs("http://api.map.baidu.com/location/ip?ip=$ip&ak=$AK").send { res -> kotlin.run {
                    if (res.succeeded()) {
                        ok(ctx,res.result().bodyAsJsonObject())
                    } else {
                        err(ctx,"request his address error.")
                    }
                } }
            }
        }
    }

}