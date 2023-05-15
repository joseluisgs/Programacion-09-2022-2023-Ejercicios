package com.example.piedrapapeltijera

import com.example.piedrapapeltijera.controllers.PiedraPapelTijeraController
import com.example.piedrapapeltijera.routes.RoutesManager.app
import com.example.piedrapapeltijera.routes.RoutesManager.initMainStage
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class PiedraPapelTijeraApplication : Application() {
    override fun start(stage: Stage) {
        app = this@PiedraPapelTijeraApplication
        initMainStage(stage)
    }

}

fun main() {
    Application.launch(PiedraPapelTijeraApplication::class.java)
}