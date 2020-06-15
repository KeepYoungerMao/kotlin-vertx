package com.mao

import java.io.IOException
import java.io.InputStream
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

object PropertiesReader {

    fun readServer(path: String) : Server {
        return Server(read(path))
    }

    private fun read(path: String) : Properties {
        val path2 = if (path.startsWith("/")) path else "/$path"
        val properties = Properties()
        val inputStream: InputStream? = PropertiesReader::class.java.getResourceAsStream(path2)
        try {
            properties.load(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return properties
    }

}