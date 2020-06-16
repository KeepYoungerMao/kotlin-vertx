package com.mao.annotation

import com.mao.util.SU

/**
 * 参数封装
 */
object Check {

    /**
     * 参数检查
     * 主要检查使用参数校验注解的类
     * 具体注解参见 com.mao.annotation.* 的参数校验注解部分
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