package com.mao.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class ResponseData<T>(val code: Int, val msg: String, val Data: T)

enum class ResEnum(val code: Int, val msg: String) {
    OK(200,"ok"),
    PERMISSION(401,"no permission"),
    NOTFOUND(404,"not found"),
    NOTALLOWED(405,"not allowed"),
    ERROR(500,"request error")
}

object Response {
    fun ok(data: Any?) : String = json(data, ResEnum.OK)
    fun permission(msg: String) : String = json(msg, ResEnum.PERMISSION)
    fun notfound(msg: String) : String = json(msg, ResEnum.NOTFOUND)
    fun notAllowed(msg: String) : String = json(msg, ResEnum.NOTALLOWED)
    fun error(msg: String) : String = json(msg, ResEnum.ERROR)
}

inline fun <reified T> json(data: T, type: ResEnum) : String {
    return jacksonObjectMapper().writeValueAsString(
        ResponseData(
            type.code,
            type.msg,
            data
        )
    )
}