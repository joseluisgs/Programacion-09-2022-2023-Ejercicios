package com.example.loschinos

import com.example.javafxdemo.routers.RoutesManager
import com.example.javafxdemo.routers.Views
import com.example.loschinos.di.AppDIModule
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {
    override fun start(stage: Stage) {
        startKoin {
            modules(AppDIModule)
        }

        RoutesManager.apply {
            app = this@MyApp
        }

        RoutesManager.setStage(stage)
        RoutesManager.addScreen(
            Views.HOME,
            600.0,
            320.0
        )
        RoutesManager.addScreen(
            Views.ACERCA_DE,
            300.0,
            200.0
        )
        RoutesManager.changeScene(
            Views.HOME,
            "Los Chinos"
        )
    }
}

fun main() {
    Application.launch(MyApp::class.java)
}