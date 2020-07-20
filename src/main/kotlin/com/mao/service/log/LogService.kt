package com.mao.service.log

import com.mao.entity.log.RequestLog

interface LogService {

    companion object {
        val INSTANCE: LogService by lazy { LogServiceImpl() }
    }

    /**
     * 保存请求日志
     */
    fun saveRequestLog(log: RequestLog)

}