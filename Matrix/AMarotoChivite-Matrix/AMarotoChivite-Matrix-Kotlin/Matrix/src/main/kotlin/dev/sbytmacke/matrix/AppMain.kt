package dev.sbytmacke.matrix

import dev.sbytmacke.matrix.di.ModuleKoin
import dev.sbytmacke.matrix.routes.RouterManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class AppMain : Application() {
    override fun start(stage: Stage) {

        startKoin {
            printLogger()
            modules(ModuleKoin)
        }

        RouterManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(AppMain::class.java)
}