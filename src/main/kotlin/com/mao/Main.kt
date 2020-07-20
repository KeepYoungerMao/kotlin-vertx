package com.mao

import com.mao.init.SystemInit
import com.mao.server.ApiServer
import io.vertx.core.*
import kotlin.system.measureTimeMillis

class Main {

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            val time = measureTimeMillis {
                SystemInit.licence()
                Vertx.vertx().deployVerticle(ApiServer())
            }
            println("Service on... [Time consumption: ${time/1000F}s]")
        }

    }

}