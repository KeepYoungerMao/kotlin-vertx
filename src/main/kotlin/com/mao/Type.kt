package com.mao

import io.vertx.core.http.HttpMethod

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
 * 分发参数的处理
 * 资源类型转化
 * 操作类型转化
 * 数据类型转化
 * 数据处理方式类型转化
 * 请求方式是否正确的判断
 */
object TypeOperation {

    fun operationType(operation: String?) : OperationType {
        return try {
            OperationType.valueOf(operation?.toUpperCase()?:"ERROR")
        } catch (e: Exception) {
            OperationType.ERROR
        }
    }

    fun dataType(data: String?) : DataType {
        return try {
            DataType.valueOf(data?.toUpperCase()?:"ERROR")
        } catch (e: Exception) {
            DataType.ERROR
        }
    }

    fun dataMethod(type: String?) : DataMethod {
        return try {
            DataMethod.valueOf(type?.toUpperCase()?:"ERROR")
        } catch (e: Exception) {
            DataMethod.ERROR
        }
    }

    fun canRequest(type: OperationType, method: HttpMethod) : Boolean {
        return when (type) {
            OperationType.SEARCH -> method == HttpMethod.GET
            OperationType.SAVE -> method == HttpMethod.PUT
            OperationType.EDIT -> method == HttpMethod.POST
            OperationType.REMOVE -> method == HttpMethod.DELETE
            else -> false
        }
    }

}

/**
 * 参数封装
 */
object CheckUtil {

    /**
     * 参数检查
     * 主要检查使用参数校验注解的类
     * 具体注解参见 com.mao.config.Annotation.kt 的参数校验注解部分
     * @param param 需要校验的类
     * @param ignore 需要忽视的字段名称
     */
    fun check(param: Any, vararg ignore: String) : String? {
        val fields = param::class.java.declaredFields
        fields.forEach {
            it.isAccessible = true
            val name = it.name
            val value = it.get(param)?.toString()
            if (!ignore.contains(name)) {
                val annotations = it.declaredAnnotations
                if (annotations.isNotEmpty()){
                    annotations.forEach { a ->
                        run {
                            if (a is NeedNumber) {
                                if (null == value || !SU.isNumber(value))
                                    return "invalid param[$name] : $value. it must be a number."
                            } else if (a is NeedNotNull) {
                                if (null == value || value.isBlank())
                                    return "param[$name] must be not null"
                            } else if (a is NeedRangeLength) {
                                if (null != value && value.length > a.max)
                                    return "param[$name] cannot longer than ${a.max} length."
                            } else if (a is NeedRangeText) {
                                if (null != value && value.toByteArray().size > a.max.times(1024))
                                    return "param[$name] cannot bigger than ${a.max}KB."
                            }
                        }
                    }
                }
            }
        }
        return null
    }

}

object SU {
    fun isNumber(str: String) : Boolean {
        return try {
            str.toLong() > 0
        } catch (e: Exception) {
            false
        }
    }
}