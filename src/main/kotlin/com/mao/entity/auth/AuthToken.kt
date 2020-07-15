package com.mao.entity.auth

/**
 * token
 */
data class AuthToken(val access_token: String,
                       val refresh_token: String,
                       var expire: Long,
                       val timestamp: Long)