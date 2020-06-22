package com.mao.type

import io.vertx.core.http.HttpMethod

/**
 * 处理 /api/data/:operation/:data/:method 的请求URL参数的转化
 * 将URL参数转化为ENUM类型，转化失败返回ERROR类型
 * 转化类型详见 com.mao.enum.DataType.kt
 */
object Operation {

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

    fun hisType(type: String?) : HisType {
        return try {
            HisType.valueOf(type?.toUpperCase()?:"ERROR")
        } catch (e: Exception) {
            HisType.ERROR
        }
    }

}