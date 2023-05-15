package dev.sergiosf.chinos

import dev.sergiosf.chinos.routes.RoutersManager
import javafx.application.Application
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        RoutersManager.apply {
            app = this@HelloApplication
        }
        RoutersManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}