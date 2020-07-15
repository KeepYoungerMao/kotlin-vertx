package com.mao.entity.response

/**
 * 统一结构返回体
 */
data class ResponseData<T>(val code: Int, val msg: String, val Data: T)