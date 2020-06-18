package com.mao.data

import java.util.*

class Server(properties: Properties) {

    val name: String = properties.getProperty("server.name")
    val link: String = properties.getProperty("server.link")
    val status: String = "ok"
    val version: String = properties.getProperty("server.version")
    val description: String = properties.getProperty("server.description")
    val ip: String = properties.getProperty("server.ip")
    val port: Int = properties.getProperty("server.port").toInt()
    val start: Long = System.currentTimeMillis()

}