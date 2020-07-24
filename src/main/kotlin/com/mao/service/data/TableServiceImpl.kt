package com.mao.service.data

import com.mao.entity.data.DataTable
import com.mao.server.ApiServer
import com.mao.service.BaseService
import com.mao.util.SU
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

class TableServiceImpl : TableService, BaseService() {

    override fun handle(ctx: RoutingContext) {
        when (ctx.request().method()) {
            HttpMethod.GET -> getData(ctx)
            HttpMethod.POST -> refuse(ctx,"The service is temporarily unavailable.")
            HttpMethod.PUT -> refuse(ctx,"The service is temporarily unavailable.")
            HttpMethod.DELETE -> refuse(ctx,"The service is temporarily unavailable.")
            else -> refuse(ctx,"request method[${ctx.request().method()}] not allowed.")
        }
    }

    private fun getData(ctx: RoutingContext) {
        val type = ctx.request().getParam("type")
        if (SU.isEmpty(type))
            err(ctx,"loss param : type.")
        else {
            when (type) {
                "table" -> ok(ctx,getTable(ctx.request().getParam("table")))
                "tables" -> ok(ctx,getTables())
                else -> refuse(ctx,"type : $type is not allowed.")
            }
        }
    }

    /**
     * 获取单表详情，携带字段数据
     */
    private fun getTable(table: String?) : DataTable? {
        if (null == table) return null
        ApiServer.dataTable.forEach { if (table == it.name) return it }
        return null
    }

    /**
     * 获取所有表数据，但不提供字段数据
     */
    private fun getTables() : MutableList<DataTable> {
        val list: MutableList<DataTable> = ArrayList()
        ApiServer.dataTable.forEach { list.add(DataTable(it.id,it.name,it.table,it.main,it.main_id,ArrayList())) }
        return list
    }

}