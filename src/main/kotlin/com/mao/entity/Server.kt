package com.mao.entity

import java.util.*

/**
 * 服务器数据
 */
class Server(properties: Properties) {
    private val name: String = properties.getProperty("server.name")
    private val link: String = properties.getProperty("server.link")
    private val status: String = "ok"
    private val version: String = properties.getProperty("server.version")
    private val description: String = properties.getProperty("server.description")
    private val ip: String = properties.getProperty("server.ip")
    val port: Int = properties.getProperty("server.port").toInt()
    val authorize: Boolean = properties.getProperty("server.authorize")!!.toBoolean()
    private val start: Long = System.currentTimeMillis()
    val dataCenter: Long = properties.getProperty("server.data_center").toLong()
    val machine: Long = properties.getProperty("server.machine").toLong()

    override fun toString(): String {
        return """
            |-------------------------------------------------------------------------
            | server name         |  $name
            | server link         |  $link
            | server version      |  $version
            | server description  |  $description
            | server ip           |  $ip
            | server port         |  $port
            | server authorize    |  $authorize
            | server start        |  $start
            | server data center  |  $dataCenter
            | server machine      |  $machine
            | server status       |  $status
            |-------------------------------------------------------------------------
            | > > > > > ------ service start ------ < < < < < <
            |-------------------------------------------------------------------------
        """.trimIndent()
    }
}