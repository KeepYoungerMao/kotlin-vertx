package com.mao.entity.data

data class DataTable(
    val id: Long,                           //主键
    val name: String,                       //数据名
    val table: String,                      //表名
    val main: Boolean,                      //是否为主表
    val main_id: String?,                   //如果不为主表，则主表主键在该表的字段名称
    val columns: MutableList<DataColumn>    //表下字段列表
)