package com.example.piedrapapeltijera.routes

import com.example.piedrapapeltijera.PiedraPapelTijeraApplication
import com.example.piedrapapeltijera.controllers.PiedraPapelTijeraController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage

object RoutesManager {

    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    val activeStage get() = _activeStage
    lateinit var app: Application
    private val iconoApp = Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/iconoApp.png"))
    private val iconoReglas = Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/iconoReglas.png"))

    fun initMainStage(stage: Stage) {
        val fxmlLoader = FXMLLoader(PiedraPapelTijeraApplication::class.java.getResource("views/principal-view.fxml"))
        val controlador = PiedraPapelTijeraController()
        fxmlLoader.setController(controlador)
        val scene = Scene(fxmlLoader.load(), 510.0, 355.0)
        stage.title = "Piedra Papel Tijeras Lagarto Spock"
        stage.scene = scene
        stage.isResizable = false
        mainStage = stage
        _activeStage = stage
        stage.icons.add(iconoApp)
        stage.show()
    }

    fun reglasStage() {
        val fxmlLoader = FXMLLoader(PiedraPapelTijeraApplication::class.java.getResource("views/reglas-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Reglas del juego"
        stage.scene = scene
        stage.isResizable = false
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.icons.add(iconoReglas)
        stage.show()
    }
}