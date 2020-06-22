package com.mao.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * 配置文件的读取
 * Properties文件读取
 * json文件读取
 */
object FileReader {

    /**
     * 读取服务器数据
     */
    fun readServer(path: String) : Server {
        val properties = Properties()
        try {
            properties.load(getInputStream(path))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Server(properties)
    }

    /**
     * 读取表数据
     */
    fun readTable(path: String) : MutableList<DataTable> {
        try {
            val reader = BufferedReader(InputStreamReader(getInputStream(path)))
            val sb = StringBuilder()
            reader.forEachLine { sb.append(it) }
            return jacksonObjectMapper().readValue(sb.toString())
        } catch (e: Exception) {
            throw RuntimeException("read data table error")
        }
    }

    /**
     * 获取inputStream
     */
    private fun getInputStream(path: String) : InputStream {
        val path2 = if (path.startsWith("/")) path else "/$path"
        return FileReader::class.java.getResourceAsStream(path2)
    }

}