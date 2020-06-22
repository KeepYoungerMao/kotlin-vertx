package com.mao.sql

import com.mao.data.Response.ok
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
class Query {

    fun execute(query: String, single: Boolean, commit: Boolean, handler: Handler<AsyncResult<String>>) {
        ApiServer.jdbcClient.getConnection { res -> kotlin.run {
            if (res.succeeded()) {
                val connection = res.result()
                if (commit) {
                    connection.update(query) { update -> kotlin.run {
                        if (update.succeeded()) {
                            connection.commit { status -> kotlin.run {
                                if (status.succeeded()) {
                                    handler.handle(Future.succeededFuture(ok("process completed successfully.")))
                                } else {
                                    handler.handle(Future.failedFuture("commit failed."))
                                }
                            }}
                        } else {
                            handler.handle(Future.failedFuture("update or save data filed."))
                        }
                    }}
                } else {
                    connection.query(query) { result -> kotlin.run {
                        if (result.succeeded()) {
                            val resultRows = result.result().rows
                            if (resultRows.isEmpty()) {
                                handler.handle(Future.succeededFuture(ok(null)))
                            } else {
                                if (single) {
                                    handler.handle(Future.succeededFuture(ok(resultRows[0].map)))
                                } else {
                                    val list = ArrayList<Map<String, Any>>()
                                    resultRows.forEach { e -> list.add(e.map) }
                                    handler.handle(Future.succeededFuture(ok(list)))
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