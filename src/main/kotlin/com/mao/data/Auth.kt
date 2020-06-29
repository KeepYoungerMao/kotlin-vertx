package com.mao.data

/**
 * 客户端
 */
data class AuthClient(val client_id: String,
                      val client_secret: String,
                      val client_name: String,
                      val enabled: Boolean,
                      val locked: Boolean,
                      val expired: Boolean,
                      val authority: MutableList<String>,
                      val ips: MutableList<String>,
                      val access_token_validation: Long,
                      val refresh_token_validation: Long)

/**
 * token
 */
data class AuthToken(val access_token: String,
                       val refresh_token: String,
                       var expire: Long,
                       val timestamp: Long)