package com.mao.server

import com.mao.data.*
import com.mao.service.BaseService
import com.mao.service.RootService
import com.mao.service.auth.AuthClient
import com.mao.service.auth.AuthService
import com.mao.service.data.DataService
import com.mao.service.data.DataTable
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
        val server: Server = Reader.readServer("/config/server.properties")
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
        router.route().handler(RootService())
        if (server.authorize)
            router.route("/oauth/:type").handler(AuthService.create())
        router.route("/api/data/:data/:type").handler(BodyHandler.create()).handler(DataService.create())
        router.mountSubRouter("/api/his",hisRouter())
        errorAdvise(router)
        vertx.createHttpServer().requestHandler(router).listen(server.port)
    }

    private fun hisRouter() : Router {
        val router = Router.router(vertx)
        router.route("/address/ip").handler(IPAddressService.create())
        router.route("/weather").handler(WeatherService.create())
        router.route("/sudoku").handler(BodyHandler.create()).handler(SudokuService.create())
        return router
    }

    private fun errorAdvise(router: Router) {
        router.errorHandler(404) { if (!it.response().ended()) BaseService().no(it) }
        router.errorHandler(405) { if (!it.response().ended()) BaseService().refuse(it) }
        router.errorHandler(500) {ctx -> kotlin.run {
            if (!ctx.response().ended()) {
                val e = ctx.failure()
                if (null != e && e is SecurityException)
                    BaseService().auth(ctx,e.message?:"permission error")
                else {
                    e?.printStackTrace()
                    BaseService().err(ctx, "request error")
                }
            }
        }}
    }

}