package com.mao.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mao.util.SU
import com.mao.util.Secret
import kotlin.system.exitProcess

/**
 * licence
 */
data class Licence(val app_id: String, val app_name: String, val start: Long, val time: Long, val key: String)

object SystemInit {

    fun licence(key: String) {
        if (key.isBlank() || key.length != 16) initError()
        val data = Reader.readLicence("/LICENCE")
        if (data.isBlank()) initError()
        try {
            val decrypt = Secret.decrypt(data, key)!!
            val point = decrypt.length - 6
            val res = Secret.decrypt(SU.dt(decrypt.substring(0,point)), key)!!
            licence(jacksonObjectMapper().readValue(res),decrypt.substring(point))
        } catch (e: Exception) {
            initError()
        }
    }

    private fun licence(licence: Licence, key: String) {
        val current = System.currentTimeMillis()
        if (current < licence.start) initError()
        if (current > (licence.start + licence.time)) initError()
        if (key != licence.key) initError()
        //println(licence)
    }

    fun initError() {
        println("Service failed to start. Please call the developer for processing")
        exitProcess(0)
    }

    /**
     * licence builder
     * {"app_id":"10001","app_name":"KEEP YOUNGER SERVICE","start":1593426895612,"time":31536000000,"key":"SYSTEM"}
     * key: MAOZX12345SYSTEM
     */
    @Deprecated("for building licence test")
    fun makeLicence(licence: Licence, key: String) {
        val json = jacksonObjectMapper().writeValueAsString(licence)
        val encrypt = Secret.encrypt(json, key)?.toUpperCase()
        val second = SU.td(encrypt!!) + "SYSTEM"
        println(encrypt)
        println(second)
        val three = Secret.encrypt(second,key)!!.toUpperCase()
        println(three)
        var count = 1
        three.forEach { c -> run {
            print("$c ")
            count ++
            if (count == 35) {
                println()
                count = 1
            }
        } }
        println()
        val decrypt = Secret.decrypt(three,key)
        val four = decrypt!!.substring(0, decrypt.length-6)
        println(decrypt)
        println(four)
        val five = SU.dt(four)
        val six = Secret.decrypt(five,key)!!
        println(five)
        println(six)
    }

}