package com.mao.service.data

import com.mao.server.ApiServer
import com.mao.service.data.entity.DataColumn
import com.mao.service.data.entity.DataTable
import com.mao.service.data.entity.DataType
import io.vertx.core.json.JsonObject

object DataCache {

    private const val TABLE_SQL = "SELECT `id`,`name`,`table`,`main`,`main_id` FROM data_root_table"
    private const val COLUMN_SQL = "SELECT `id`,pid,`column`,`type`,len,src_show,src_key,list_show,list_key,page_show,page_key,`order` FROM data_root_column"

    fun initData()  {
        initDataTables()
    }

    private fun initDataTables() {
        ApiServer.jdbcClient.getConnection { res -> kotlin.run {
            if (res.succeeded()) {
                val connection = res.result()
                connection.query(TABLE_SQL) { result -> kotlin.run {
                    if (result.succeeded()) {
                        val resultRows = result.result().rows
                        if (resultRows.isNotEmpty()) {
                            addDataTable(resultRows)
                            ApiServer.jdbcClient.getConnection { res2 -> kotlin.run {
                                if (res.succeeded()) {
                                    val connection2 = res2.result()
                                    connection2.query(COLUMN_SQL) { result2 -> kotlin.run {
                                        if (result2.succeeded()) {
                                            val resultRows2 = result2.result().rows
                                            if (resultRows2.isNotEmpty()) {
                                                addDataColumn(resultRows2)
                                                successPl()
                                            } else {
                                                throw RuntimeException("read data failed. data[ data_root_column ] is empty.")
                                            }
                                        } else {
                                            throw RuntimeException("read data failed. request data[ data_root_column ] failed.")
                                        }
                                    }}
                                    connection2.close()
                                }
                            } }
                        } else {
                            throw RuntimeException("read data failed. data[ data_root_table ] is empty.")
                        }
                    } else {
                        throw RuntimeException("read data failed. request data[ data_root_table ] failed.")
                    }
                }}
                connection.close()
            } else {
                throw RuntimeException("read data failed. connect mysql failed.")
            }
        } }
    }

    private fun addDataTable(obj : MutableList<JsonObject>) {
        try {
            obj.forEach { e -> kotlin.run {
                val map: MutableMap<String, Any> = e.map
                ApiServer.dataTable.add(
                    DataTable(
                        map["id"] as Long,
                        map["name"] as String,
                        map["table"] as String,
                        map["main"] as Boolean,
                        if (map["main_id"] == null || map["main_id"] == "") null else map["main_id"] as String,
                        ArrayList()
                    )
                )
            } }
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    private fun addDataColumn(obj : MutableList<JsonObject>) {
        try {
            obj.forEach { e -> kotlin.run {
                val map: MutableMap<String, Any> = e.map
                val column = DataColumn(
                    map["id"] as Long,
                    map["pid"] as Long,
                    map["column"] as String,
                    DataType.valueOf(map["type"] as String),
                    map["len"] as Int,
                    map["src_show"] as Boolean,
                    map["src_key"] as Boolean,
                    map["list_show"] as Boolean,
                    map["list_key"] as Boolean,
                    map["page_show"] as Boolean,
                    map["page_key"] as Boolean,
                    map["order"] as Boolean
                )
                var add = false
                for (table in ApiServer.dataTable) {
                    if (table.id == column.pid) {
                        table.columns.add(column)
                        add = true
                        break
                    }
                }
                if (!add)
                    throw Exception("data[ data_root_column ] error. no pid in data_root_table.")
            } }
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    private fun successPl() {
        println(
            """ 
             ____   ____   ____   ____   ____   ____ 
            /_   | /_   | /_   | /_   | /_   | /_   |
             |   |  |   |  |   |  |   |  |   |  |   |
             |   |  |   |  |   |  |   |  |   |  |   |
             |___|  |___|  |___|  |___|  |___|  |___|
            """.trimIndent()
        )
    }

}