package com.example.ejerciciobolaschinasivanrc

import com.example.ejerciciobolaschinasivanrc.di.diModule
import com.example.ejerciciobolaschinasivanrc.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MainApplication : Application(), KoinComponent {
    override fun start(stage: Stage) {

        startKoin {
            modules(diModule)
        }

        RoutesManager.apply {
            app = this@MainApplication
        }
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}