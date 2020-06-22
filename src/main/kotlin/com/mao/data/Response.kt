package com.mao.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * 统一结构返回体
 */
data class ResponseData<T>(val code: Int, val msg: String, val Data: T)

/**
 * 返回状态
 */
enum class ResEnum(val code: Int, val msg: String) {
    OK(200,"ok"),
    PERMISSION(401,"no permission"),
    NOTFOUND(404,"not found"),
    NOTALLOWED(405,"not allowed"),
    ERROR(500,"request error")
}

/**
 * 数据返回快捷方法
 */
object Response {
    fun ok(data: Any?) : String = json(data, ResEnum.OK)
    fun permission(msg: String) : String = json(msg, ResEnum.PERMISSION)
    fun notfound(msg: String) : String = json(msg, ResEnum.NOTFOUND)
    fun notAllowed(msg: String) : String = json(msg, ResEnum.NOTALLOWED)
    fun error(msg: String) : String = json(msg, ResEnum.ERROR)
}

/**
 * ResponseData转化为json
 * 内联方法
 */
inline fun <reified T> json(data: T, type: ResEnum) : String {
    return jacksonObjectMapper().writeValueAsString(ResponseData(type.code, type.msg, data))
}