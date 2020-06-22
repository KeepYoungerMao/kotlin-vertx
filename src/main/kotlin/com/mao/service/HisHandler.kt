package com.mao.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mao.data.Response
import com.mao.data.WeatherResult
import com.mao.server.ApiServer
import com.mao.type.HisType
import com.mao.type.Operation
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import java.io.*
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream

interface HisHandler : Handler<RoutingContext> {
    companion object {
        fun create() : HisHandler = HisHandlerImpl()
    }
}

class HisHandlerImpl : HisHandler {

    companion object {
        const val AK = "Po86Y8fZwYv5fpcQIX7MVk1DaMOl3VwB"
    }

    override fun handle(ctx: RoutingContext) {
        val data = ctx.pathParam("data")
        when (Operation.hisType(data)) {
            HisType.WEATHER -> weatherCity(ctx)
            HisType.ADDRESS -> ipAddress(ctx)
            HisType.SUDOKU -> sudoku(ctx)
            else -> ctx.response().end(Response.notAllowed("not support his data type: $data"))
        }
    }

    /**
     * 根据城市名称获取该城市简要天气信息
     */
    private fun weatherCity(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.GET)
            ctx.response().end(Response.notAllowed("request method ${ctx.request().method().name} not allowed"))
        else {
            val city = ctx.request().getParam("city")
            if (null == city)
                ctx.response().end(Response.error("loss param city"))
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
                            ctx.response().end(Response.error("request his weather error."))
                        else {
                            transWeatherData(result)
                            ctx.response().end(Response.ok(result))
                        }
                    } else {
                        ctx.response().end(Response.error("request his weather error."))
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

    /**
     * 根据ip地址获取该ip地址所在地址
     */
    private fun ipAddress(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.GET)
            ctx.response().end(Response.notAllowed("request method ${ctx.request().method().name} not allowed"))
        else {
            val ip = ctx.request().getParam("ip")
            if (null == ip)
                ctx.response().end(Response.error("loss param ip"))
            else {
                ApiServer.webClient.getAbs("http://api.map.baidu.com/location/ip?ip=$ip&ak=$AK").send { res -> kotlin.run {
                    if (res.succeeded()) {
                        ctx.response().end(res.result().bodyAsString())
                    } else {
                        ctx.response().end(Response.error("request his address error."))
                    }
                } }
            }
        }
    }

    /**
     * 数独解析，给定一组9*9的二维数组，未完成的地方使用0代替
     * 返回最多5种可能的结果。
     * 具体计算方法参见SudoKu
     * @see com.mao.service.SudoKu 数独解析
     */
    private fun sudoku(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.POST)
            ctx.response().end(Response.notAllowed("request method ${ctx.request().method().name} not allowed"))
        else {
            val array = try {
                jacksonObjectMapper().readValue<Array<Array<Int>>>(ctx.bodyAsString)
            } catch (e: Exception) {
                null
            }
            val check = checkSudoKu(array)
            if (null != check)
                ctx.response().end(Response.error(check))
            else {
                val sudoKu = SudoKu()
                sudoKu.analyse(array)
                ctx.response().end(Response.ok(sudoKu.result))
            }
        }
    }

    /**
     * 数独数据的检查
     */
    private fun checkSudoKu(array: Array<Array<Int>>?) : String? {
        if (null == array || array.isEmpty() || array.size > 9 )
            return "invalid sudoKu array."
        array.forEach { i -> run {
            i.forEach {
                if (it < 0 || it > 9)
                    return "invalid sudoKu array."
            }
        } }
        return null
    }

}