package com.mao.util

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Secret {

    private const val AES = "AES"

    /**
     * 加密
     */
    fun encrypt(data: String, key: String) : String? {
        if (data.isEmpty()) return null
        val cipher = Cipher.getInstance(AES)
        val keySpec = SecretKeySpec(key.toByteArray(), AES)
        cipher.init(Cipher.ENCRYPT_MODE,keySpec)
        val encrypt = cipher.doFinal(data.toByteArray())
        return parseByteToHexStr(encrypt)!!
    }

    /**
     * 解密
     */
    fun decrypt(data: String, key: String) : String? {
        if (data.isEmpty()) return null
        val array = parseHexStrToByte(data) ?: return null
        val cipher = Cipher.getInstance(AES)
        val keySpec = SecretKeySpec(key.toByteArray(), AES)
        cipher.init(Cipher.DECRYPT_MODE,keySpec)
        val decrypt = cipher.doFinal(array)
        return String(decrypt)
    }

    /**
     * byte转16进制字符
     */
    private fun parseByteToHexStr(array: ByteArray) : String? {
        if (array.isEmpty()) return null
        val sb = StringBuilder()
        array.forEach {
            val hex = Integer.toHexString(it.toInt() and 0xFF)
            if (hex.length < 2) sb.append(0)
            sb.append(hex)
        }
        return sb.toString()
    }

    /**
     * 16进制字符转byte
     */
    private fun parseHexStrToByte(str: String) : ByteArray? {
        if (str.isEmpty()) return null
        val hex = str.toUpperCase()
        val len = hex.length/2
        val chars = hex.toCharArray()
        val d = ByteArray(len)
        for (i in 0 until len) {
            val pos = i*2
            d[i] = (charToByte(chars[pos]) shl 4 or charToByte(chars[pos + 1])).toByte()
        }
        return d
    }

    private fun charToByte(c: Char) : Int {
        return "0123456789ABCDEF".indexOf(c).toByte().toInt()
    }

}