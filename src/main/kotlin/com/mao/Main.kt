package com.mao

import com.mao.handler.ApiHandler
import io.vertx.core.*

class Main {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Vertx.vertx().deployVerticle(ApiHandler())
            println("project start ... ")
        }
    }

}