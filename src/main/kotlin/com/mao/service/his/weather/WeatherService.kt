package com.mao.service.his.weather

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface WeatherService : Handler<RoutingContext> {

    companion object {
        fun create() : WeatherService = WeatherServiceImpl()
    }

}