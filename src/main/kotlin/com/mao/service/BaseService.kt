package com.mao.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mao.entity.response.ResEnum
import com.mao.entity.response.ResponseData
import com.mao.server.ApiServer
import com.mao.entity.auth.AuthClient
import com.mao.entity.auth.AuthToken
import com.mao.entity.log.RequestLog
import com.mao.service.log.LogService
import com.mao.util.SU
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

open class BaseService {

    companion object {
        //clientId : authToken
        val cachedAuth: MutableMap<String, AuthToken> = HashMap()
        //access_token : clientId
        val cachedToken: MutableMap<String, String> = HashMap()
        //refresh_token : clientId
        val cachedRefresh: MutableMap<String, String> = HashMap()

        const val ID = "id"
        const val AUTHORIZATION = "Authorization"
        const val TYPE_ERR = "type error. no this type of resource data."
        const val INVALID_AUTHORIZATION = "invalid Authorization code."
        const val NEED_AUTHORIZATION = "need Authorization."
        const val INVALID_CLIENT = "client cannot use."
        const val CLIENT_EXPIRED = "client expired."
        const val CLIENT_LOCKED = "client locked."
        const val TOKEN_EXPIRED = "token expired."
        const val SELF_IP6 = "0:0:0:0:0:0:0:1"
        const val SELF_IP4 = "127.0.0.1"
    }

    private val logService: LogService = LogService.INSTANCE

    /**
     * 授权
     */
    protected fun verify(code: String?)  {
        if (ApiServer.server.authorize) {
            if (null == code || code.isEmpty())
                auth(NEED_AUTHORIZATION)
            else {
                val clientId = cachedToken[code]
                if (null == clientId)
                    auth(INVALID_AUTHORIZATION)
                else {
                    val check = check(clientId)
                    if (null != check)
                        auth(check)
                }
            }
        }
    }

    /**
     * 权限检查
     * 1. 暂时未写具体权限校验
     * 2. 由于token缓存无失效机制，超过时间的token只能在该client在此请求api时在此删除，
     *      或者服务重启。（当然，该client重新请求token会覆盖之前的数据）
     */
    private fun check(clientId: String) : String? {
        val client = getClient(clientId)
        val token = cachedAuth[clientId]
        if (null == client) return INVALID_AUTHORIZATION
        if (!client.enabled) return INVALID_CLIENT
        if (client.expired) return CLIENT_EXPIRED
        if (client.locked) return CLIENT_LOCKED
        if (null == token) return INVALID_AUTHORIZATION
        val now = SU.now()
        if ((now - token.timestamp) > client.access_token_validation) {
            cachedToken.remove(token.access_token)
            cachedRefresh.remove(token.refresh_token)
            cachedAuth.remove(clientId)
            return TOKEN_EXPIRED
        }
        return null
    }

    /**
     * 获取客户端
     */
    protected fun getClient(id: String) : AuthClient? {
        ApiServer.authClient.forEach { if (it.client_id == id) return it }
        return null
    }

    /**
     * 成功返回
     */
    fun ok(ctx: RoutingContext, data: Any?) {
        end(ctx,ResEnum.OK,data)
    }

    /**
     * 返回访问受限
     */
    fun refuse(ctx: RoutingContext) {
        refuse(ctx,"request not allowed.")
    }

    /**
     * 访问受限
     */
    fun refuse(ctx: RoutingContext, msg: String) {
        end(ctx,ResEnum.NOTALLOWED,msg)
    }

    /**
     * 错误返回
     */
    fun err(ctx: RoutingContext, msg: String) {
        end(ctx,ResEnum.ERROR,msg)
    }

    /**
     * 无权限，抛出异常
     */
    private fun auth(msg: String) {
        throw SecurityException(msg)
    }

    /**
     * 权限错误返回
     */
    fun auth(ctx: RoutingContext, msg: String) {
        end(ctx,ResEnum.PERMISSION,msg)
    }

    /**
     * 地址错误
     */
    fun no(ctx: RoutingContext) {
        end(ctx,ResEnum.NOTFOUND,"no resource path: ${ctx.request().path()}")
    }

    /**
     * 统一返回方法，可在此执行统一的其他逻辑
     */
    private fun end(ctx: RoutingContext, resEnum: ResEnum, data: Any?) {
        log(ctx,resEnum.code)
        ctx.response().end(json(data,resEnum))
    }

    /**
     * ResponseData转化为json
     * 内联方法
     */
    private inline fun <reified T> json(data: T, type: ResEnum) : String {
        return jacksonObjectMapper().writeValueAsString(ResponseData(type.code, type.msg, data))
    }

    /**
     * 请求日志的保存
     */
    private fun log(ctx: RoutingContext, status: Int) {
        val id = ApiServer.idBuilder.nextId()
        val ip = ctx.request().remoteAddress().host()
        val path = ctx.request().path()
        val method = methodNumber(ctx.request().method())
        val params = SU.json(ctx.request().params())
        val body = ctx.bodyAsString
        val user = if (ApiServer.server.authorize) {
            val code = ctx.request().getHeader(AUTHORIZATION)
            cachedToken[code]
        } else null
        val ips = if (ip == SELF_IP6) SU.ipToLong(SELF_IP4) else SU.ipToLong(ip)
        val log = RequestLog(id,ips,path,method,params,body,user,status,SU.now())
        logService.saveRequestLog(log)
    }

    /**
     * 数据保存时请求方式采用数字形式保存
     * get 1
     * post 2
     * put 3
     * delete 4
     * 其它不被允许，记作0
     */
    private fun methodNumber(method: HttpMethod) : Int {
        return when (method) {
            HttpMethod.GET -> 1
            HttpMethod.POST -> 2
            HttpMethod.PUT -> 3
            HttpMethod.DELETE -> 4
            else -> 0
        }
    }

}