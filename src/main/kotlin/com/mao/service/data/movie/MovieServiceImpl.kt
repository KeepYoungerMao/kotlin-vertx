package com.mao.service.data.movie

import com.mao.service.data.DataTableEnum
import com.mao.service.data.DataService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class MovieServiceImpl : MovieService, DataService() {

    override fun handle(ctx: RoutingContext) {
        val type: String = ctx.pathParam(TYPE)
        when (ctx.request().method()) {
            HttpMethod.GET -> {
                when(type) {
                    SRC_TYPE -> movieSrc(ctx)
                    LIST_TYPE -> movieList(ctx)
                    PAGE_TYPE -> moviePage(ctx)
                    CLASSIFY_TYPE -> movieClassify(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.POST -> {
                when(type) {
                    SRC_TYPE -> updateMovie(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.PUT -> {
                when(type) {
                    SRC_TYPE -> saveMovie(ctx)
                    LIST_TYPE -> saveMovies(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            HttpMethod.DELETE -> {
                when(type) {
                    SRC_TYPE -> deleteMovie(ctx)
                    LIST_TYPE -> deleteMovies(ctx)
                    else -> refuse(ctx, TYPE_ERR)
                }
            }
            else -> refuse(ctx, ONLY_SUPPORT_METHOD_ERR)
        }
    }

    //查询电影详情
    private fun movieSrc(ctx: RoutingContext) {
        execute(ctx, single = true, commit = false, args = ctx.request().getParam("id")) { sqlBuilder.movieSrc(it) }
    }

    //检索电影列表
    private fun movieList(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.movieList(it) }
    }

    //分页查询电影列表
    private fun moviePage(ctx: RoutingContext) {
        execute(ctx, single = false, commit = false, args = ctx.request().params()) { sqlBuilder.moviePage(it) }
    }

    //查询电影分类数据
    private fun movieClassify(ctx: RoutingContext) {
        sqlResult(ctx,sqlBuilder.dataDict(DataTableEnum.MOVIE.name),single = false,commit = false)
    }

    //保存电影数据
    private fun saveMovie(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.saveMovie(it) }
    }

    //保存多个电影数据
    private fun saveMovies(ctx: RoutingContext) {
        execute(ctx, single = false, commit = true, args = ctx.bodyAsJsonArray) { sqlBuilder.saveMovies(it) }
    }

    //更新电影数据
    private fun updateMovie(ctx: RoutingContext) {
        execute(ctx, single = true, commit = true, args = ctx.bodyAsJson) { sqlBuilder.updateMovie(it) }
    }

    //删除电影数据，接收电影id参数
    private fun deleteMovie(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

    //删除多个电影数据，接收id拼接字符串
    private fun deleteMovies(ctx: RoutingContext) {
        refuse(ctx, NO_OPEN)
    }

}