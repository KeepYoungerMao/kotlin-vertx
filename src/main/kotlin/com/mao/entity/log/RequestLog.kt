package com.mao.entity.log

/**
 * 请求日志
 */
data class RequestLog(
    val id: Long,           //id
    val ip: String,         //用户操作ip
    val path: String,       //请求地址
    val method: String,     //请求方式
    val params: String?,      //请求参数
    val body: String?,       //请求body参数
    val user: String?,       //用户
    val status: Int,        //请求状态
    val date: String,       //请求日期
    val time: Long          //请求时间戳
)