package com.jczhang.jczhangbolaschinaskotlinjavafx

import com.jczhang.jczhangbolaschinaskotlinjavafx.routes.RouterManager
import javafx.application.Application
import javafx.stage.Stage


class BolasApplication : Application() {

    override fun start(stage: Stage) {
        RouterManager.apply {
            app = this@BolasApplication
        }
        RouterManager.mainStageInit(stage)
    }
}

fun main() {
    Application.launch(BolasApplication::class.java)
}