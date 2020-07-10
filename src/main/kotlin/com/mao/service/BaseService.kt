package com.mao.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mao.data.ResEnum
import com.mao.data.ResponseData
import com.mao.server.ApiServer
import com.mao.service.auth.AuthClient
import com.mao.service.auth.AuthToken
import com.mao.util.SU
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
    }

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

    protected fun getClient(id: String) : AuthClient? {
        ApiServer.authClient.forEach { if (it.client_id == id) return it }
        return null
    }

    fun ok(ctx: RoutingContext, data: Any) {
        ctx.response().end(json(data, ResEnum.OK))
    }

    fun refuse(ctx: RoutingContext) {
        refuse(ctx,"request not allowed.")
    }

    fun refuse(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg, ResEnum.NOTALLOWED))
    }

    fun err(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg, ResEnum.ERROR))
    }

    private fun auth(msg: String) {
        throw SecurityException(msg)
    }

    fun auth(ctx: RoutingContext, msg: String) {
        ctx.response().end(json(msg,ResEnum.PERMISSION))
    }

    fun no(ctx: RoutingContext) {
        ctx.response().end(json("no resource path: ${ctx.request().path()}",ResEnum.NOTFOUND))
    }

    /**
     * ResponseData转化为json
     * 内联方法
     */
    private inline fun <reified T> json(data: T, type: ResEnum) : String {
        return jacksonObjectMapper().writeValueAsString(ResponseData(type.code, type.msg, data))
    }

}