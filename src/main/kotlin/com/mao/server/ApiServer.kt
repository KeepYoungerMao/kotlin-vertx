package com.mao.server

import com.mao.data.*
import com.mao.service.AuthHandler
import com.mao.service.BaseService
import com.mao.service.ErrorHandler
import com.mao.service.auth.AuthClient
import com.mao.service.auth.AuthService
import com.mao.service.data.DataTable
import com.mao.service.data.bjx.BjxService
import com.mao.service.data.book.BookService
import com.mao.service.data.buddhist.BuddhistService
import com.mao.service.data.live.LiveService
import com.mao.service.data.movie.MovieService
import com.mao.service.data.pic.PicService
import com.mao.service.his.bd.IPAddressService
import com.mao.service.his.sudoku.SudokuService
import com.mao.service.his.weather.WeatherService
import com.mao.util.Reader
import com.mao.util.SnowFlake
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.handler.BodyHandler

class ApiServer : AbstractVerticle() {

    companion object {
        private val server: Server = Reader.readServer("/config/server.properties")
        val authClient: MutableList<AuthClient> = Reader.readClient("/config/client.json")
        val dataTable: MutableList<DataTable> = Reader.readTable("/config/data_table.json")
        private val config: Config = Reader.readConfig("/config/config.properties")
        val jdbcClient: JDBCClient = JDBCClient.createShared(Vertx.vertx(), Reader.readJDBC("/config/jdbc.properties"))
        val webClient: WebClient = WebClient.create(
            Vertx.vertx(), WebClientOptions().setUserAgent(config.userAgent).setKeepAlive(config.keepAlive)
        )
        val idBuilder: SnowFlake = SnowFlake(config.dataCenter, config.machine)
    }

    override fun start() {
        val router = Router.router(vertx)
        router.route("/").handler { BaseService().ok(it, server) }
        router.route().handler(AuthHandler.create())
        router.route("/oauth/:type").handler(AuthService.create())
        router.mountSubRouter("/api/data",dataRouter())
        router.mountSubRouter("/api/his",hisRouter())
        router.errorHandler(404, ErrorHandler.created(404))
        router.errorHandler(405, ErrorHandler.created(405))
        router.errorHandler(500, ErrorHandler.created(500))
        vertx.createHttpServer().requestHandler(router).listen(server.port)
    }

    private fun dataRouter() : Router {
        val router = Router.router(vertx)
        router.route("/bjx/:type").handler(BodyHandler.create()).handler(BjxService.create())
        router.route("/book/:type").handler(BodyHandler.create()).handler(BookService.create())
        router.route("/buddhist/:type").handler(BodyHandler.create()).handler(BuddhistService.create())
        router.route("/live/:type").handler(BodyHandler.create()).handler(LiveService.create())
        router.route("/movie/:type").handler(BodyHandler.create()).handler(MovieService.create())
        router.route("/pic/:type").handler(BodyHandler.create()).handler(PicService.create())
        return router
    }

    private fun hisRouter() : Router {
        val router = Router.router(vertx)
        router.route("/address/ip").handler(IPAddressService.create())
        router.route("/weather").handler(WeatherService.create())
        router.route("/sudoku").handler(BodyHandler.create()).handler(SudokuService.create())
        return router
    }

}