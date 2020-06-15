package com.mao

import com.mao.Response.ok
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler

class Query {

    fun execute(query: String, single: Boolean, commit: Boolean, handler: Handler<AsyncResult<String>>) {
        MainVerticle.jdbcClient.getConnection { res -> kotlin.run {
            if (res.succeeded()) {
                val connection = res.result()
                if (commit) {
                    connection.update(query) { update -> kotlin.run {
                        if (update.succeeded()) {
                            connection.commit { status -> kotlin.run {
                                if (status.succeeded()) {
                                    handler.handle(Future.future { success -> success.complete("update or save success") })
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
                                handler.handle(Future.succeededFuture(""))
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