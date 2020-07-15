package com.mao

import com.mao.init.SystemInit
import com.mao.server.ApiServer
import io.vertx.core.*
import kotlin.system.measureTimeMillis

class Main {

    companion object {

        private const val INIT = "sysInit="

        @JvmStatic fun main(args: Array<String>) {
            val time = measureTimeMillis {
                system(args)
                Vertx.vertx().deployVerticle(ApiServer())
            }
            println("Service on... [Time consumption: ${time/1000F}s]")
        }

        private fun system(args: Array<String>){
            if (args.isEmpty()) SystemInit.initError()
            var thr = true
            args.forEach {
                if (it.startsWith(INIT)) {
                    thr = false
                    SystemInit.licence(it.substring(INIT.length))
                }
            }
            if (thr) SystemInit.initError()
        }

    }

}