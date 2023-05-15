package dev.sbytmacke.thechinos

import dev.sbytmacke.thechinos.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage

class MainApp : Application() {
    override fun start(stage: Stage) {
        // Inicializamos el manager de navegación (Routes) para encapsular los STAGES y SCENES
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}