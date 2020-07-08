package com.mao.service.his.sudoku

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mao.service.BaseService
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class SudokuServiceImpl : SudokuService, BaseService() {

    override fun handle(ctx: RoutingContext) {
        if (ctx.request().method() != HttpMethod.POST)
            refuse(ctx,"request method ${ctx.request().method().name} not allowed")
        else {
            val array: Array<Array<Int>>? = try {
                jacksonObjectMapper().readValue<Array<Array<Int>>>(ctx.bodyAsString)
            } catch (e: Exception) {
                null
            }
            val check: String? = checkSudoKu(array)
            if (null != check)
                err(ctx,check)
            else {
                val sudoKu = SudoKu()
                sudoKu.analyse(array)
                ok(ctx,sudoKu.result)
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