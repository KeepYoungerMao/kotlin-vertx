package com.mao.server

import com.mao.entity.Server
import com.mao.service.BaseService
import com.mao.service.RootService
import com.mao.entity.auth.AuthClient
import com.mao.service.auth.AuthService
import com.mao.init.DataCache
import com.mao.service.data.DataService
import com.mao.entity.data.DataTable
import com.mao.service.data.TableService
import com.mao.util.Reader
import com.mao.util.SnowFlake
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

class ApiServer : AbstractVerticle() {

    companion object {
        val server: Server = Reader.readServer("/config/server.properties")
        val authClient: MutableList<AuthClient> = Reader.readClient("/config/client.json")
        val jdbcClient: JDBCClient = JDBCClient.createShared(Vertx.vertx(),Reader.readJDBC("/config/jdbc.properties"))
        val dataTable: MutableList<DataTable> = ArrayList()
        val idBuilder: SnowFlake = SnowFlake(server.dataCenter, server.machine)
    }

    override fun start() {
        DataCache.initData()
        val router = Router.router(vertx)
        router.route("/").handler { BaseService().ok(it, server) }
        router.route().handler(RootService())
        if (server.authorize)
            router.route("/oauth/:type").handler(AuthService.create())
        router.route("/api/data/:data/:type").handler(BodyHandler.create()).handler(DataService.create())
        router.route("/api/table").handler(BodyHandler.create()).handler(TableService.create())
        errorAdvise(router)
        vertx.createHttpServer().requestHandler(router).listen(server.port)
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
                    BaseService().err(ctx, e?.message?:"request error")
                }
            }
        }}
    }

}