package com.mao.server

import com.mao.data.*
import com.mao.service.AuthHandler
import com.mao.service.DataHandler
import com.mao.service.ErrorHandler
import com.mao.service.HisHandler
import com.mao.util.SnowFlake
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.handler.BodyHandler

/**
 * kotlin + vertX
 * Vertx.vertx()报错：
 * Calls to static methods in Java interfaces are prohibited in JVM target 1.6. Recompile with '-jvm-target 1.8'
 * file -> setting -> builder -> Compiler -> kotlin Compiler -> Target jvm version = 1.8
 * File -> Project Structure -> Module -> 项目下kotlin -> target platform = 1.8 并且勾选 use project settings
 *
 * 使用vertx-jdbc-client的问题：
 * 回现：正常请求几次之后，浏览器请求一直pending，不动
 * 原因：当JDBCClient获取SQLConnection后，执行query，并成功返回，如果SQLConnection没有关闭，便不能回收，连接池用完，则会一直pending
 *
 * 使用BodyHandler：
 * BodyHandler默认不会将表单参数合并到body主体中
 *
 * 使用handler的方式：
 * 1. 使用静态方法模式：
 *      router.route("/test").handler(Test::testHandler);
 *
 *      private static void testHandler(RoutingContext ctx) {
 *          ctx.response.end("ok");
 *      }
 *
 * 2. 使用类实现 Handler<RoutingContext> 接口
 *   这种是在vertX中默认使用的，而且都是使用接口继承Handler接口，再由实现类实现方法，在接口中定义创建实例的静态方法
 *      interface Test extend Handler<RoutingContext> {
 *          static void create() { return new TestImpl(); }
 *      }
 *
 *      class TestImpl implements Test {
 *          @override
 *          public void handler(RoutingContext ctx) {
 *              ctx.response().end("ok");
 *          }
 *      }
 * vertX中默认使用的都是类方法，且是如：new BodyHandlerImpl()，没有所谓的单例。这样请求来了，难道不耗费时间么？
 * 在查看后可以看出：启动后，请求访问之前，Router对象已经被创建，handler的类也已经完成创建，请求访问时没有类的创建。
 *
 * 关于companion object：
 * 目前所了解的只适用于静态处理。
 * 在route的handler处理中，vertX提供的一些基本处理如：BodyHandler.create()
 * 源码中只是：return new BodyHandlerImpl();
 * 我想继承此思想：route的handler处理都采用：interface.create() 的处理方式
 * 但是每次处理都 new 一个对象感觉开销太大
 * 于是便找了些单例的使用方法：
 * 1. 单例 加锁
 *      private var instance: DataHandler? = null
 *      fun create() : DataHandler = instance?: synchronized(this) {
 *          instance?: DataHandlerImpl().apply { instance = this }
 *      }
 * 2. kotlin中的懒加载：
 *      val instance: DrugSrc by lazy {  DrugSrcImpl() }
 * 测试正常。
 * 但后来就不用担心了，多虑了，程序启动后，Router会被加载，加载过程中，每个handler都被加载了，之后便不会再进行创建。
 * 因此handler中的Handler对象都只创建了一次，根本不需要考虑单例的问题。
 *
 * 但这种单例的方式还是有用武之地的
 * 比如：我使用了Query对象进行对SQL的查询，也是采用 interface.create() 的创建方式
 * 这个时候如果有多个Handler对象引用了此类，则每个Handler对象中的Query对象都是不同得
 * 这个时候Query对象就可以使用单例的方式了。
 */
class ApiServer : AbstractVerticle() {

    companion object {
        private val server: Server = FileReader.readServer("/config/server.properties")
        val authClient: MutableList<AuthClient> = FileReader.readClient("/config/client.json")
        val dataTable: MutableList<DataTable> = FileReader.readTable("/config/data_table.json")
        private val config: Config = FileReader.readConfig("/config/config.properties")
        val jdbcClient: JDBCClient = JDBCClient.createShared(
            Vertx.vertx(), JsonObject()
            .put("url", config.url + config.urlParam)
            .put("driver_class", config.driver)
            .put("user", config.username)
            .put("password", config.password)
        )
        val webClient: WebClient = WebClient.create(
            Vertx.vertx(), WebClientOptions().setUserAgent(config.userAgent).setKeepAlive(config.keepAlive)
        )
        val idBuilder: SnowFlake = SnowFlake(config.dataCenter, config.machine)
    }

    override fun start() {
        val router = Router.router(vertx)
        router.route("/").handler { it.response().end(Response.ok(server)) }
        router.route().handler(AuthHandler.create())
        router.route("/api/data/:operation/:data/:method").handler(BodyHandler.create()).handler(DataHandler.created())
        router.route("/api/his/:data").handler(BodyHandler.create()).handler(HisHandler.create())
        router.errorHandler(404, ErrorHandler.created(404))
        router.errorHandler(405, ErrorHandler.created(405))
        router.errorHandler(500, ErrorHandler.created(500))
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }

}