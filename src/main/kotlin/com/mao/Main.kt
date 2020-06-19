package com.mao

import com.mao.server.ApiServer
import io.vertx.core.*

class Main {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Vertx.vertx().deployVerticle(ApiServer())
            println("project start ... ")
        }
    }

}