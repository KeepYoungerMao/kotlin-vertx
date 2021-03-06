package com.mao.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mao.entity.auth.AuthClient
import com.mao.entity.Server
import io.vertx.core.json.JsonObject
import java.io.*
import java.util.*

/**
 * 配置文件的读取
 * Properties文件读取
 * json文件读取
 */
object Reader {

    /**
     * 读取JDBC
     */
    fun readJDBC(path: String) : JsonObject {
        val properties = getProperties(path)
        if (properties.isEmpty) throw RuntimeException("properties is empty")
        val res = JsonObject()
        properties.forEach { k, v -> res.put(k.toString(),v.toString()) }
        return res
    }

    /**
     * 读取服务器数据
     */
    fun readServer(path: String) : Server {
        return Server(getProperties(path))
    }

    /**
     * 读取auth client 数据
     */
    fun readClient(path: String) : MutableList<AuthClient> {
        try {
            return jacksonObjectMapper().readValue(getString(path))
        } catch (e: Exception) {
            throw RuntimeException("read auth client error")
        }
    }

    /**
     * 读取licence
     */
    fun readLicence(path: String) : String {
        val reader = BufferedReader(InputStreamReader(getInputStream(path)))
        val sb = StringBuilder()
        reader.forEachLine {
            if (it.isNotBlank() && !it.startsWith("#"))
                sb.append(it.replace(" ",""))
        }
        return sb.toString()
    }

    /**
     * 读取json文件
     * 返回string
     */
    private fun getString(path: String) : String {
        val reader = BufferedReader(InputStreamReader(getInputStream(path)))
        val sb = StringBuilder()
        reader.forEachLine { sb.append(it) }
        return sb.toString()
    }

    /**
     * 读取properties文件
     * 返回Properties
     */
    private fun getProperties(path: String) : Properties {
        val properties = Properties()
        try {
            properties.load(getInputStream(path))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return properties
    }

    /**
     * 获取inputStream
     */
    private fun getInputStream(path: String) : InputStream {
        val path2 = if (path.startsWith("/")) path else "/$path"
        return FileReader::class.java.getResourceAsStream(path2)
    }

}