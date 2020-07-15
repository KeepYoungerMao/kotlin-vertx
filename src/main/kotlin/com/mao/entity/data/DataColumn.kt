package com.mao.entity.data

data class DataColumn(
    val id: Long,               //主键
    val pid: Long,              //表id
    val column: String,         //字段名
    val type: DataType,         //字段类型
    val len: Int,               //该字段允许最大长度，STRING表示长度，TEXT表示大小，BOOLEAN不限制，字段表示默认是或否
    val src_show: Boolean,      //详情查询时是否展示
    val src_key: Boolean,       //详情查询时是否作为查询字段
    val list_show: Boolean,     //列表搜索时是否展示
    val list_key: Boolean,      //列表搜索时是否作为查询字段
    val page_show: Boolean,     //分页查询时是否展示
    val page_key: Boolean,      //分页查询时是否作为查询字段
    val order: Int,             //是否作为排序字段
    val save: Boolean           //保存时是否必须
)