package com.jczhang.jczhangcocheskotlinjavafx

import com.jczhang.jczhangcocheskotlinjavafx.di.AppDIModule
import com.jczhang.jczhangcocheskotlinjavafx.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {  }

class CochesApplication : Application() {
    override fun start(stage: Stage) {
        logger.debug { "Iniciando CochesApplication" }

        startKoin{
            slf4jLogger()
            modules(AppDIModule)
        }

        RoutesManager.apply {
            app = this@CochesApplication
        }
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(CochesApplication::class.java)
}