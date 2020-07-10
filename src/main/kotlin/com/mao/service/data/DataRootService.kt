package com.mao.service.data

import com.mao.service.BaseService
import com.mao.sql.Query
import io.vertx.core.Handler
import io.vertx.core.MultiMap
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.lang.Exception

open class DataRootService : BaseService() {

    companion object {

        const val DATA = "data"
        const val TYPE = "type"

        const val DATA_BJX = "bjx"
        const val DATA_BOOK = "book"
        const val DATA_BUDDHIST = "buddhist"
        const val DATA_LIVE = "live"
        const val DATA_MOVIE = "movie"
        const val DATA_PIC = "pic"

        const val SRC_TYPE = "src"
        const val LIST_TYPE = "list"
        const val PAGE_TYPE = "page"
        const val CHAPTER_TYPE = "chapter"
        const val CHAPTERS_TYPE = "chapters"
        const val CLASSIFY_TYPE = "classify"

        const val ONLY_SUPPORT_METHOD_ERR = "request method not allowed. only support: GET,POST,PUT,DELETE."
        const val NO_OPEN = "This service is not available"
        const val DATA_TYPE_ERR = "not support this data type"
    }

    private val query : Query = Query.INSTANCE
    protected val sqlBuilder: SqlBuilder = SqlBuilder.INSTANCE

    /**
     * sql执行：数据输出， 单条记录输出
     * 获取参数 id 进行查询
     */
    fun output(ctx: RoutingContext, sqlMethod: (arg: String?) -> String) {
        execute(ctx,single = true, commit = false, args = ctx.request().getParam(ID), sqlMethod = sqlMethod)
    }

    /**
     * sql执行：数据输出， 多条记录输出
     * 获取参数 map 进行查询
     */
    fun outputs(ctx: RoutingContext, sqlMethod: (arg: MultiMap) -> String) {
        execute(ctx,single = false, commit = false, args = ctx.request().params(), sqlMethod = sqlMethod)
    }

    /**
     * 特殊sql执行：数据输出，多条记录输出
     * 获取id，该id为父类id，获取该id所有字类列表
     */
    fun children(ctx: RoutingContext, sqlMethod: (arg: String?) -> String) {
        execute(ctx, single = false, commit = false, args = ctx.request().getParam(ID), sqlMethod = sqlMethod)
    }

    /**
     * sql执行：数据输入（包括更新和保存，不包括删除）， 单条记录输入
     * 获取 body 参数 ，body 参数为对象形式
     */
    fun input(ctx: RoutingContext, sqlMethod: (arg: JsonObject) -> String) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson, sqlMethod = sqlMethod)
    }

    /**
     * sql执行：数据输入（只包括保存，不支持多条记录更新，不包括删除）， 多条条记录输入
     * 获取 body 参数 ，body 参数为集合形式，包含多个对象
     */
    fun inputs(ctx: RoutingContext, sqlMethod: (arg: JsonArray) -> String) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray, sqlMethod = sqlMethod)
    }

    /**
     * 特殊sql执行：字典数据查询
     * 携带参数：表名
     */
    fun dict(ctx: RoutingContext, table: String) {
        sqlResult(ctx,sqlBuilder.dataDict(table),single = false,commit = false)
    }


    /**
     * 数据请求、数据操作统一方法
     */
    private fun <T> execute(ctx: RoutingContext, single: Boolean, commit: Boolean,
                    args: T, sqlMethod: (arg: T) -> String) {
        verify(ctx.request().getHeader(AUTHORIZATION))
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
    private fun sqlResult(ctx: RoutingContext, sql: String, single: Boolean, commit: Boolean) {
        query.execute(sql,single,commit,handler = Handler { res -> kotlin.run {
            if (res.succeeded()) {
                ok(ctx,res.result())
            } else {
                err(ctx,"query database error.")
            }
        } })
    }

}