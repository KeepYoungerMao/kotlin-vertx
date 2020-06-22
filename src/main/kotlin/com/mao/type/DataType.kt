package com.mao.type

/**
 * 请求类型
 * SEARCH：      GET请求
 * SAVE：        PUT请求
 * EDIT：        post请求
 * REMOVE：      delete请求
 * 请求类型错误时转化至ERROR
 */
enum class OperationType {
    ERROR, SEARCH, SAVE, EDIT, REMOVE
}

/**
 * 数据类型
 * BOOK：        古籍
 * BJX：         百家姓
 * BUDDHIST：    佛经
 * LIVE：        直播源
 * MOVIE：       电影
 * PIC：         图片
 * 数据类型错误时转化至ERROR
 */
enum class DataType {
    ERROR, BOOK, BJX, BUDDHIST, LIVE, MOVIE, PIC
}

/**
 * 数据处理方式类型
 * SRC：         单个数据
 * LIST：        多个数据
 * PAGE：        分页数据
 * CHAPTER：     特殊使用：章节数据
 * CHAPTER：     特殊使用：多个章节数据
 * CLASSIFY：    分类数据
 * 处理方式类型错误时转化至ERROR
 */
enum class DataMethod {
    ERROR, SRC, LIST, PAGE, CHAPTER, CHAPTERS, CLASSIFY
}

/**
 * his请求类型
 * WEATHER：天气数据请求，通过传递城市名称city获取天气数据
 * ADDRESS：根据ip获取该ip所在地址数据
 * SUDOKU：数独解析
 */
enum class HisType {
    WEATHER,
    ADDRESS,
    SUDOKU,
    ERROR
}