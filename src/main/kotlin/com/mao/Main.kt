package com.mao

import io.vertx.core.*
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router

/**
 * kotlin + vertX
 * Vertx.vertx()报错：
 * Calls to static methods in Java interfaces are prohibited in JVM target 1.6. Recompile with '-jvm-target 1.8'
 * file -> setting -> builder -> Compiler -> kotlin Compiler -> Target jvm version = 1.8
 * File -> Project Structure -> Module -> 项目下kotlin -> target platform = 1.8 并且勾选 use project settings
 * 使用vertx-jdbc-client的问题：
 * 回现：正常请求几次之后，浏览器请求一直pending，不动
 * 原因：当JDBCClient获取SQLConnection后，执行query，并成功返回，如果SQLConnection没有关闭，便不能回收，连接池用完，则会一直pending
 */
class MainVerticle : AbstractVerticle() {

    companion object {

        var jdbcClient: JDBCClient = JDBCClient.createShared(
                Vertx.vertx(), JsonObject()
                .put("url","jdbc:mysql://localhost:3306/keep_younger?characterEncoding=utf-8&serverTimezone=Asia/Shanghai")
                .put("driver_class","com.mysql.cj.jdbc.Driver")
                .put("user","root")
                .put("password","root")
        )

        val dataOperation = DataOperation.created()

        @JvmStatic fun main(args: Array<String>) {
            Vertx.vertx().deployVerticle(MainVerticle())
            println("project start ... ")
        }

    }

    override fun start() {
        val router = Router.router(vertx)
        router.route().handler {
            it.response().putHeader("content-type","application/json charset=utf-8")
            it.next()
        }
        router.route("/api/data/:operation/:data/:method").handler(dataOperation)
        router.errorHandler(404,ErrorHandler.created(404))
        router.errorHandler(405,ErrorHandler.created(405))
        router.errorHandler(500,ErrorHandler.created(500))
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }

}

