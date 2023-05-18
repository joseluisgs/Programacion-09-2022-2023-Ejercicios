package com.example.gestionvehiculosconimagenes_kotlin

import com.example.gestionvehiculosconimagenes_kotlin.di.vehiculoModule
import com.example.gestionvehiculosconimagenes_kotlin.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class MainApplication : Application(){

    init {
        startKoin {
            modules(vehiculoModule)
        }
    }

    override fun start(stage: Stage) {
        RoutesManager.apply{
            app = this@MainApplication
        }
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}