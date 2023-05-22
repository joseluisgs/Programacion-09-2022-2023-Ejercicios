package dev.kevin.vehiculosfx

import dev.kevin.vehiculosfx.di.AppDiModule
import dev.kevin.vehiculosfx.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class HelloApplication : Application() {
    init {
        startKoin { modules(AppDiModule) }
    }
    override fun start(stage: Stage) {
        RoutesManager.apply {
            app = this@HelloApplication
        }
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}