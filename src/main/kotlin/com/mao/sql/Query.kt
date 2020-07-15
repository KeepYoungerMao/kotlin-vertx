package com.mao.sql

import io.vertx.core.AsyncResult
import io.vertx.core.Handler

interface Query {

    companion object {
        val INSTANCE: Query by lazy { QueryImpl() }
    }

    fun execute(query: String, single: Boolean, commit: Boolean, handler: Handler<AsyncResult<Any>>)

}