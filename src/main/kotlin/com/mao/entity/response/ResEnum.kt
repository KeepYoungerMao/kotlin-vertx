package com.mao.entity.response

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