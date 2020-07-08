package com.mao.service.data

import com.mao.service.BaseService
import com.mao.sql.Query
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import java.lang.Exception

open class DataService : BaseService() {

    companion object {

        const val TYPE = "type"

        const val SRC_TYPE = "src"
        const val LIST_TYPE = "list"
        const val PAGE_TYPE = "page"
        const val CHAPTER_TYPE = "chapter"
        const val CHAPTERS_TYPE = "chapters"
        const val CLASSIFY_TYPE = "classify"

        const val ONLY_SUPPORT_METHOD_ERR = "request method not allowed. only support: GET,POST,PUT,DELETE."
        const val NO_OPEN = "this api has been no open"
    }

    private val query : Query = Query.INSTANCE
    protected val sqlBuilder: SqlBuilder = SqlBuilder.INSTANCE

    /**
     * 数据请求、数据操作统一方法
     */
    fun <T> execute(ctx: RoutingContext, single: Boolean, commit: Boolean,
                    args: T, sqlMethod: (arg: T) -> String) {
        var sql: String? = null
        try {
            sql = sqlMethod(args)
        } catch (e: Exception) {
            err(ctx,e.message?:"request error")
        }
        if (null != sql )
            sqlResult(ctx, sql, single, commit)
    }

    /**
     * 统一执行sql语句，并操作RoutingContext返回数据
     * 执行sql语句也是同一调用execute方法
     * @param ctx RoutingContext上下文
     * @param sql 执行的SQL语句
     * @param single 在SQL语句为查询类语句时使用，为true时表示只查询一条记录，false表示查询多条记录
     * @param commit 是否需要提交，需要提交的表示为更新、保存、删除类型SQL语句
     */
    fun sqlResult(ctx: RoutingContext, sql: String, single: Boolean, commit: Boolean) {
        query.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ok(ctx,res.result())
            } else {
                err(ctx,"query database error.")
            }
        } })
    }

}