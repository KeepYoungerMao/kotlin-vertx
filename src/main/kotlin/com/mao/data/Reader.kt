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
        return Server(read(path))
    }

    private fun read(path: String) : Properties {
        val path2 = if (path.startsWith("/")) path else "/$path"
        val properties = Properties()
        val inputStream: InputStream? = FileReader::class.java.getResourceAsStream(path2)
        try {
            properties.load(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return properties
    }

    /**
     * 读取表数据
     */
    fun readTable(path: String) : MutableList<DataTable> {
        try {
            return jacksonObjectMapper().readValue(read2(path))
        } catch (e: Exception) {
            throw RuntimeException("read data table error")
        }
    }

    private fun read2(path: String) : String {
        val path2 = if (path.startsWith("/")) path else "/$path"
        val inputStream = FileReader::class.java.getResourceAsStream(path2)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        reader.forEachLine { sb.append(it) }
        return sb.toString()
    }

}