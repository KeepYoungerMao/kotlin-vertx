package com.mao.data

data class AuthClient(val client_id: String,
                      val client_secret: String,
                      val client_name: String,
                      val enabled: Boolean,
                      val locked: Boolean,
                      val state: String,
                      val redirect_uri: String,
                      val grant_type: MutableList<String>,
                      val scope: MutableList<String>,
                      val access_token_validation: Long,
                      val refresh_token_validation: Long)

data class AccessToken(val access_token: String,
                       val refresh_token: String,
                       val expire: Long,
                       val grant_type: String,
                       val scope: String)

data class CachedClient(val client_id: String, val validate_time: Long, val scope: String)