package com.mao.enum

import io.vertx.core.http.HttpMethod

/**
 * 分发参数的处理
 * 资源类型转化
 * 操作类型转化
 * 数据类型转化
 * 数据处理方式类型转化
 * 请求方式是否正确的判断
 */
object EnumOperation {

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
            OperationType.SEARCH -> method == HttpMethod.GET || method == HttpMethod.POST
            OperationType.SAVE -> method == HttpMethod.PUT
            OperationType.EDIT -> method == HttpMethod.POST
            OperationType.REMOVE -> method == HttpMethod.DELETE
            else -> false
        }
    }

}