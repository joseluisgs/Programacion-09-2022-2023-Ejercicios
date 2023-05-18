package com.example.concesionario

import com.example.concesionario.di.AppDIModule
import com.example.javafxdemo.routes.RoutesManager
import com.example.javafxdemo.routes.Views
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(AppDIModule)
        }

        RoutesManager.apply {
            app = this@MyApplication
        }

        RoutesManager.setStage(stage)
        RoutesManager.addScreen(
            Views.HOME,
            600.0,
            400.0
        )
        RoutesManager.addScreen(
            Views.FORM,
            280.0,
            350.0
        )
        RoutesManager.addScreen(
            Views.ACERCA_DE,
            400.0,
            150.0
        )
        RoutesManager.changeScene(
            Views.HOME,
            "Concesionario"
        )
    }
}

fun main() {
    Application.launch(MyApplication::class.java)
}