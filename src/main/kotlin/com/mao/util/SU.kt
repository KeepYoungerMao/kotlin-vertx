package com.mao.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mao.init.SystemInit
import io.vertx.core.MultiMap
import java.time.LocalDate

/**
 * 常用的字符串类处理和判断
 */
object SU {

    private const val RANDOM: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val TD: MutableMap<Char,CharArray> = hashMapOf(
        Pair('0', charArrayOf('+','Z','A')),
        Pair('1', charArrayOf('=','Y','9')),
        Pair('2', charArrayOf('-','B','1')),
        Pair('3', charArrayOf('#','C','L')),
        Pair('4', charArrayOf('@','0','P')),
        Pair('5', charArrayOf('H','4','X')),
        Pair('6', charArrayOf('7','8','?')),
        Pair('7', charArrayOf('E','F','^')),
        Pair('8', charArrayOf('D','K','!')),
        Pair('9', charArrayOf('$','2','G')),
        Pair('A', charArrayOf('6','M','N')),
        Pair('B', charArrayOf('5','U','V')),
        Pair('C', charArrayOf('&','S','T')),
        Pair('D', charArrayOf('3','I','J')),
        Pair('E', charArrayOf('*','R','W')),
        Pair('F', charArrayOf('%','O','Q'))
    )

    /**
     * 字符串转化:转化失败、数字为负数，则返回默认数
     */
    fun toLong(str: String?, df: Long) : Long {
        return try {
            val res = str?.toLong()?:df
            if (res < 0) df else res
        } catch (e: Exception) {
            df
        }
    }

    /**
     * 字符串转long
     */
    fun toLong(str: String?) : Long = toLong(str,-1)

    /**
     * 判断字符串是否不为空
     */
    private fun isNotEmpty(str: String?) : Boolean = null != str && str.isNotEmpty()

    /**
     * 判断字符串是否为空
     */
    fun isEmpty(str: String?) : Boolean = !isNotEmpty(str)

    /**
     * 获取随机字符串
     */
    fun randomSting(len: Int) : String {
        val array = CharArray(len)
        for (i in 0 until len)
            array[i] = RANDOM.random()
        return String(array)
    }

    /**
     * 遍历 TD
     * 将data字符串的字符匹配 key 换成 value
     */
    fun td(data: String) : String {
        val sb = StringBuilder()
        data.forEach {
            if (TD.containsKey(it)) {
                sb.append(TD[it]?.random())
            } else SystemInit.initError()
        }
        return sb.toString()
    }

    /**
     * 遍历 TD
     * 将data字符串的字符匹配 value 换成 key
     * 假设：需要匹配的字符长度为 500
     * 则最大匹配次数为：500 * 16 * 3 = 24000
     */
    fun dt(data: String) : String {
        val sb = StringBuilder()
        data.forEach { sb.append(dts(it)?:SystemInit.initError()) }
        return sb.toString()
    }

    /**
     * 常量 TD 中寻找 value 中是否存在 char
     * 有则返回 key，找不到返回 null
     */
    private fun dts(char: Char) : Char? {
        TD.forEach { (k, v) -> kotlin.run {
            val x = v.find { it == char }
            if (null != x) return k
        } }
        return null
    }

    /**
     * 获取当前日期
     * yyyy-MM-dd
     */
    fun date() : String = LocalDate.now().toString()

    /**
     * 获取当前时间戳
     * 13位时间戳
     */
    fun now() : Long = System.currentTimeMillis()

    /**
     * map转json
     */
    fun json(map: MultiMap) : String? {
        if (map.isEmpty) return null
        val res: MutableMap<String, String> = HashMap()
        map.forEach { res[it.key] = it.value }
        return jacksonObjectMapper().writeValueAsString(res)
    }

}