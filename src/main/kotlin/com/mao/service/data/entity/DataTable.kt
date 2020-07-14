package com.mao.service.data.entity

data class DataTable(
    val id: Long,
    val name: String,
    val table: String,
    val main: Boolean,
    val main_id: String?,
    val columns: MutableList<DataColumn>
)