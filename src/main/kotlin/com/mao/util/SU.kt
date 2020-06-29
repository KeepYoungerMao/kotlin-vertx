package com.mao.util

/**
 * 常用的字符串类处理和判断
 */
object SU {

    private const val RANDOM: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun isNumber(str: String) : Boolean {
        return try {
            str.toLong() > 0
        } catch (e: Exception) {
            false
        }
    }

    fun isNotNumber(str: String) : Boolean = !isNumber(str)

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

    fun toLong(str: String?) : Long = toLong(str,-1)

    fun isNotEmpty(str: String?) : Boolean = null != str && str.isNotEmpty()

    fun isEmpty(str: String?) : Boolean = !isNotEmpty(str)

    fun randomSting(len: Int) : String {
        val array = CharArray(len)
        for (i in 0 until len)
            array[i] = RANDOM.random()
        return String(array)
    }

}