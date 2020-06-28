package com.mao.service

import com.mao.data.AuthClient
import com.mao.data.CachedClient
import com.mao.data.Response
import com.mao.server.ApiServer
import com.mao.util.SU
import io.vertx.core.Handler
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext

interface AuthHandler : Handler<RoutingContext> {
    companion object {
        fun create() : AuthHandler = AuthHandlerImpl()
    }
}

/**
 * oauth2认证授权简化版
 * client表示用户，没有user概念
 * auto_improve为true：没有用户授权确认页面
 * 缓存内容：
 * access_token : client_id & start_time
 */
class AuthHandlerImpl : AuthHandler {

    private val cachedAuth: MutableMap<String, CachedClient> = HashMap()
    private val cachedCode: MutableMap<String, String> = HashMap()

    companion object {
        const val RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE_VALUE = "code"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
        const val REDIRECT_URI = "redirect_uri"
        const val AUTHORIZATION = "Authorization"
        const val AUTHORIZE_URL = "/oauth/authorize"
        const val ACCESS_TOKEN_URL = "/oauth/token"
        const val AUTHORIZATION_CODE = "authorization_code"
        const val PASSWORD = "password"
        const val REFRESH_TOKEN = "refresh_token"
        const val GRANT_TYPE = "grant_type"
    }

    override fun handle(ctx: RoutingContext) {
        when (ctx.request().path()) {
            AUTHORIZE_URL -> authorize(ctx)
            ACCESS_TOKEN_URL -> accessToken(ctx)
            else -> authentication(ctx)
        }
    }

    /**
     * response_type=code
     * 验证client_id和redirect_uri
     */
    private fun authorize(ctx: RoutingContext) {
        val responseType = ctx.request().getParam(RESPONSE_TYPE)
        val clientId = ctx.request().getParam(CLIENT_ID)
        val redirectUri = ctx.request().getParam(REDIRECT_URI)
        if (SU.isEmpty(responseType) || responseType != RESPONSE_TYPE_VALUE)
            invalidParam(ctx,RESPONSE_TYPE)
        else if (SU.isEmpty(clientId))
            lossParam(ctx, CLIENT_ID)
        else if (SU.isEmpty(redirectUri))
            lossParam(ctx, REDIRECT_URI)
        else {
            val client = getClient(clientId)
            when {
                null == client -> invalidParam(ctx, CLIENT_ID)
                client.redirect_uri != redirectUri -> invalidParam(ctx, REDIRECT_URI)
                else -> {
                    //各个参数都正确：生成code，缓存code，302转发
                    val code = SU.randomSting(6)
                    cachedCode[clientId] = code
                    ctx.response().putHeader(HttpHeaders.LOCATION,redirectUri)
                        .setStatusCode(302)
                        .end(Response.ok("redirect success"))
                }
            }
        }
    }

    private fun accessToken(ctx: RoutingContext){
        val grantType = ctx.request().getParam(GRANT_TYPE)
        val clientId = ctx.request().getParam(CLIENT_ID)
        val clientSecret = ctx.request().getParam(CLIENT_SECRET)
        when (grantType) {
            AUTHORIZATION_CODE -> {
                val code = ctx.request().getParam(RESPONSE_TYPE_VALUE)
                //if (SU.isEmpty(code))
            }
            PASSWORD -> {}
            REFRESH_TOKEN -> {}
        }
        ctx.response().end(Response.error("building"))
    }


    private fun authentication(ctx: RoutingContext) {
        val authCode = ctx.request().getHeader(AUTHORIZATION)
        if (null == authCode)
            println("request without Authorization code.")
        else
            println("request with Authorization code: $authCode")
        ctx.response().putHeader("content-type","application/json charset=utf-8")
        ctx.next()
    }

    private fun getClient(id: String) : AuthClient? {
        ApiServer.authClient.forEach { if (it.client_id == id) return it }
        return null
    }

    private fun lossParam(ctx: RoutingContext, param: String) {
        ctx.response().end(Response.error("param error: loss param $param"))
    }
    private fun invalidParam(ctx: RoutingContext, param: String) {
        ctx.response().end(Response.error("param error: invalid param $param"))
    }

}