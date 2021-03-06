package com.mao.service.auth

import com.mao.entity.auth.AuthToken
import com.mao.service.BaseService
import com.mao.util.SU
import io.vertx.ext.web.RoutingContext

class AuthServiceImpl : AuthService, BaseService() {

    companion object {
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    override fun handle(ctx: RoutingContext) {
        when (ctx.pathParam("type")) {
            ACCESS_TOKEN -> accessToken(ctx)
            REFRESH_TOKEN -> refreshToken(ctx)
            else -> refuse(ctx, TYPE_ERR)
        }
    }

    /**
     * token的获取
     * 检查clientId和clientSecret参数的正确性
     * 检查client是否正常使用
     * 检查此client之前是否获取过token，如果获取过则判断是否还可使用(需超过可用时间的三分之一)，
     * 可使用则使用该token，不可使用则新建token
     */
    private fun accessToken(ctx: RoutingContext){
        val clientId = ctx.request().getParam(CLIENT_ID)
        val clientSecret = ctx.request().getParam(CLIENT_SECRET)
        if (SU.isEmpty(clientId)) lossParam(ctx,CLIENT_ID)
        else if (SU.isEmpty(clientSecret)) lossParam(ctx,CLIENT_SECRET)
        else {
            //根据client_id获取客户端数据
            val client = getClient(clientId)
            //client需要存在，secret一致、可使用、未锁定、不过期
            if (null == client) invalidParam(ctx,CLIENT_ID)
            else if (clientSecret != client.client_secret) invalidParam(ctx,CLIENT_SECRET)
            else if (!client.enabled) permission(ctx,"client not enabled")
            else if (client.locked) permission(ctx,"client locked")
            else if (client.expired) permission(ctx,"client expired")
            else {
                //查询缓存中client是否请求过authToken
                val authToken = cachedAuth[clientId]
                val time = client.access_token_validation
                //获取返回的token
                val token = if (null != authToken) {
                    val current = timeStamp()
                    if ((current - authToken.timestamp) < time*0.9) {
                        //当前时间 - 发行时间 < 可使用时间的三分之二 ： 该token可继续使用
                        //剩余可使用时间 = 可使用时间 - （过去的时间）
                        authToken.expire = time - (current - authToken.timestamp)
                        authToken
                    } else {
                        //当前时间 - 发行时间 不够：该token需要作废。 删除当前数据，使用新的数据
                        cachedToken.remove(authToken.access_token)
                        cachedRefresh.remove(authToken.refresh_token)
                        val token = getToken(time)
                        cachedAuth[clientId] = token
                        cachedToken[token.access_token] = clientId
                        cachedRefresh[token.refresh_token] = clientId
                        token
                    }
                } else {
                    //缓存中无此client数据，使用新token返回
                    val token = getToken(time)
                    cachedAuth[clientId] = token
                    cachedToken[token.access_token] = clientId
                    cachedRefresh[token.refresh_token] = clientId
                    token
                }
                ok(ctx,token)
            }
        }
    }

    /**
     * token的刷新
     * 检查clientId和refresh_token参数的正确性
     * 缓存中存在该client的access_token
     */
    private fun refreshToken(ctx: RoutingContext) {
        val clientId = ctx.request().getParam(CLIENT_ID)
        val refreshToken = ctx.request().getParam(REFRESH_TOKEN)
        if (SU.isEmpty(clientId)) lossParam(ctx,CLIENT_ID)
        else if (SU.isEmpty(refreshToken)) lossParam(ctx,REFRESH_TOKEN)
        else {
            val c = cachedRefresh[refreshToken]
            if (null == c) invalidParam(ctx,REFRESH_TOKEN)
            else if (c != clientId) invalidParam(ctx,CLIENT_ID)
            else {
                val client = getClient(clientId)
                if (null == client) invalidParam(ctx,CLIENT_ID)
                else {
                    //refresh_token 和 client_id 都正确，组装新的token，删除原来的token
                    val old = cachedAuth[clientId]
                    if (null == old || (timeStamp() - old.timestamp) > client.access_token_validation*0.9)
                        permission(ctx,"please get a new access token.")
                    else {
                        cachedToken.remove(old.access_token)
                        cachedRefresh.remove(refreshToken)
                        val token = getToken(client.access_token_validation)
                        cachedAuth[clientId] = token
                        cachedToken[token.access_token] = clientId
                        cachedRefresh[token.refresh_token] = clientId
                        ok(ctx,token)
                    }
                }
            }
        }
    }

    private fun getToken(expire: Long) : AuthToken {
        return AuthToken(
            SU.randomSting(32),
            SU.randomSting(32),
            expire,
            timeStamp()
        )
    }
    private fun timeStamp() : Long = System.currentTimeMillis()/1000

    private fun lossParam(ctx: RoutingContext, param: String) {
        err(ctx,"param error: loss param $param")
    }
    private fun invalidParam(ctx: RoutingContext, param: String) {
        err(ctx,"param error: invalid param $param")
    }
    private fun permission(ctx: RoutingContext, message: String) {
        refuse(ctx,message)
    }

}