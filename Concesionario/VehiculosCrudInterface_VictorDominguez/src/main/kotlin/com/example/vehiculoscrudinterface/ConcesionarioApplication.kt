package com.example.vehiculoscrudinterface

import com.example.vehiculoscrudinterface.routes.RoutesManager.app
import com.example.vehiculoscrudinterface.routes.RoutesManager.initMainStage
import javafx.application.Application
import javafx.stage.Stage


class ConcesionarioApplication : Application() {
    override fun start(stage: Stage) {
        app = this@ConcesionarioApplication
        initMainStage(stage)
    }
}

fun main() {
    Application.launch(ConcesionarioApplication::class.java)
}