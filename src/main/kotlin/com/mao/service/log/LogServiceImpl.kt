package com.mao.service.log

import com.mao.entity.log.RequestLog
import com.mao.sql.Query
import io.vertx.core.Handler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Supplier

/**
 * 日志数据处理
 * 日志保存采用异步进行
 */
class LogServiceImpl : LogService {

    private val service: ExecutorService = Executors.newFixedThreadPool(4)
    private val query: Query = Query.INSTANCE

    /**
     * 请求日志的保存
     */
    override fun saveRequestLog(log: RequestLog) {
        CompletableFuture.supplyAsync(Supplier<Unit> { kotlin.run {
            val sql = """
                INSERT INTO sys_request_log(`id`,`ip`,`path`,`method`,`params`,`body`,`user`,`status`,`time`) 
                VALUE (${log.id},${log.ip},'${log.path}',${log.method},'${log.params}','${log.body}',
                '${log.user}',${log.status},${log.time})
            """.trimIndent()
            query.execute(sql,single = true,commit = true,handler = Handler { res -> kotlin.run {
                if (res.failed())
                    println("[ warning ][ com.mao.service.LogServiceImpl.saveRequestLog() ] : failed to save log.")
            }})
        } },service)
    }

}