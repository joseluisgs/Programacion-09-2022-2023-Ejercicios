package dev.kuromiichi.emmafernandezjuegocanicas

import dev.kuromiichi.emmafernandezjuegocanicas.data.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage

class CanicasApplication : Application() {
    override fun start(stage: Stage) {
        RoutesManager.apply {
            app = this@CanicasApplication
        }
        RoutesManager.initGameStage(stage)
    }
}

fun main() {
    Application.launch(CanicasApplication::class.java)
}
