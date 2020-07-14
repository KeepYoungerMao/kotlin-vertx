package com.mao.service.data.entity

data class DataColumn(
    val id: Long,
    val pid: Long,
    val column: String,
    val type: DataType,
    val len: Int,
    val src_show: Boolean,
    val src_key: Boolean,
    val list_show: Boolean,
    val list_key: Boolean,
    val page_show: Boolean,
    val page_key: Boolean,
    val order: Boolean
)