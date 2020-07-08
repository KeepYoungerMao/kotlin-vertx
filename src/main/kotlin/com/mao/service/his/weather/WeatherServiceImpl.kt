package com.mao.service.his.weather

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mao.service.his.WeatherResult
import com.mao.server.ApiServer
import com.mao.service.BaseService
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream

class WeatherServiceImpl : WeatherService, BaseService() {

    override fun handle(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.GET)
            refuse(ctx,"request method ${ctx.request().method().name} not allowed")
        else {
            val city = ctx.request().getParam("city")
            if (null == city)
                err(ctx,"loss param city")
            else {
                ApiServer.webClient.getAbs("http://wthrcdn.etouch.cn/weather_mini?city=$city").send { res -> kotlin.run {
                    if (res.succeeded()) {
                        val json = unGZip(res.result()?.body())?:""
                        val result = try {
                            jacksonObjectMapper().readValue<WeatherResult>(json)
                        } catch (e: Exception) {
                            null
                        }
                        if (null == result)
                            err(ctx,"request his weather error.")
                        else {
                            transWeatherData(result)
                            ok(ctx,result)
                        }
                    } else {
                        err(ctx,"request his weather error.")
                    }
                } }
            }

        }
    }

    /**
     * 去除<![CDATA[]]>
     */
    private fun transWeatherData(result: WeatherResult) {
        result.data.yesterday.fl = removeK(result.data.yesterday.fl)
        result.data.forecast.forEach { it.fengli = removeK(it.fengli) }
    }

    /**
     * 去除<![CDATA[]]>
     */
    private fun removeK(str: String) : String {
        val pattern = Pattern.compile(".*<!\\[CDATA\\[(.*)]]>.*")
        val matcher = pattern.matcher(str)
        return if (matcher.matches()) matcher.group(1) else str
    }

    /**
     * GZIP数据解压
     */
    private fun unGZip(buffer: Buffer?) : String? {
        if (null == buffer) return null
        val reader = try {
            BufferedReader(InputStreamReader(GZIPInputStream(ByteArrayInputStream(buffer.bytes))))
        } catch (e: IOException) {
            null
        } ?: return null
        val sb = StringBuilder()
        reader.forEachLine { sb.append(it) }
        return sb.toString()
    }

}