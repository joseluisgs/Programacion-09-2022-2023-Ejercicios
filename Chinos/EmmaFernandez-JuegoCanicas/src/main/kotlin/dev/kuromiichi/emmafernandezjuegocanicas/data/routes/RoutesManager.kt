package dev.kuromiichi.emmafernandezjuegocanicas.data.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage

object RoutesManager {
    lateinit var app: Application

    fun initGameStage(stage: Stage) {
        val fxmlLoader = FXMLLoader(app::class.java.getResource("game-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 460.0)
        stage.title = "dev.kuromiichi.emmafernandezjuegocanicas.Juego de las Canicas"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun initInstruccionesStage() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(app::class.java.getResource("instrucciones-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 460.0)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Instrucciones - dev.kuromiichi.emmafernandezjuegocanicas.Juego de las Canicas"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun initAcercaDeStage() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(app::class.java.getResource("acerca-de-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 460.0)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Acerca de - dev.kuromiichi.emmafernandezjuegocanicas.Juego de las Canicas"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}
