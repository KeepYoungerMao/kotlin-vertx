package com.mao.sql

import com.mao.server.ApiServer
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler

/**
 * SQL执行方法。异步返回。
 * single判断是否返回单条数据，如果是则返回resultRows中的第一条
 * commit表示是否需要提交：
 * 方法默认查询数据不需要提交，返回查询的结果；
 * 更新、保存、删除数据时需要提交，返回是否成功。
 */
class QueryImpl : Query {

    /**
     * SQL统一执行方法
     * 异步执行
     * 根据single和commit参数判断执行逻辑和返回数据
     * single：是否为单条数据，目前当commit为true的时候不做判断。
     *      在commit为false的时候起作用。
     *      查询单条数据是返回单条数据
     *      查询多条数据时返回多条数据
     * commit：是否需要提交：此参数为true时表示此SQL为非查询类SQL
     *      需要执行提交操作。此时返回成功或失败的信息提示数据
     * 返回数据统一为ResponseData类型的json字符串。
     */
    override fun execute(query: String, single: Boolean, commit: Boolean, handler: Handler<AsyncResult<Any>>) {
        ApiServer.jdbcClient.getConnection { res -> kotlin.run {
            if (res.succeeded()) {
                val connection = res.result()
                if (commit) {
                    connection.setAutoCommit(true) {}
                    connection.update(query) { update -> kotlin.run {
                        if (update.succeeded()) {
                            handler.handle(Future.succeededFuture("process completed successfully."))
                        } else {
                            handler.handle(Future.failedFuture("update or save data filed."))
                        }
                    }}
                } else {
                    connection.query(query) { result -> kotlin.run {
                        if (result.succeeded()) {
                            val resultRows = result.result().rows
                            if (resultRows.isEmpty()) {
                                handler.handle(Future.succeededFuture(null))
                            } else {
                                if (single) {
                                    handler.handle(Future.succeededFuture(resultRows[0].map))
                                } else {
                                    val list = ArrayList<Map<String, Any>>()
                                    resultRows.forEach { e -> list.add(e.map) }
                                    handler.handle(Future.succeededFuture(list))
                                }
                            }
                        } else {
                            //println(result.cause().message)
                            handler.handle(Future.failedFuture("query database failed."))
                        }
                    }}
                }
                connection.close()
            } else {
                handler.handle(Future.failedFuture("query database error."))
            }
        } }
    }

}