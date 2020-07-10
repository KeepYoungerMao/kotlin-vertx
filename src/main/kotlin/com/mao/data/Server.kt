package com.mao.data

import java.util.*

/**
 * 服务器数据
 */
class Server(properties: Properties) {
    val name: String = properties.getProperty("server.name")
    val link: String = properties.getProperty("server.link")
    val status: String = "ok"
    val version: String = properties.getProperty("server.version")
    val description: String = properties.getProperty("server.description")
    val ip: String = properties.getProperty("server.ip")
    val port: Int = properties.getProperty("server.port").toInt()
    val authorize: Boolean = properties.getProperty("server.authorize").toBoolean()
    val start: Long = System.currentTimeMillis()
}

/**
 * 服务器配置参数
 */
class Config(properties: Properties) {
    val userAgent: String = properties.getProperty("web.client.user_agent")
    val keepAlive: Boolean = properties.getProperty("web.client.keep_alive")?.toBoolean()?:false
    val dataCenter: Long = properties.getProperty("server.endpoint.data_center").toLong()
    val machine: Long = properties.getProperty("server.endpoint.machine").toLong()
}