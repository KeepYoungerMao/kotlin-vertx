package com.mao.service.his.sudoku

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

interface SudokuService : Handler<RoutingContext> {

    companion object {
        fun create() : SudokuService = SudokuServiceImpl()
    }

}