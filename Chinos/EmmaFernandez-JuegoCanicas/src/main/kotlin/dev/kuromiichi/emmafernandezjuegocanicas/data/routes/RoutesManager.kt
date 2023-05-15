package dev.kuromiichi.emmafernandezjuegocanicas.data.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.InputStream
import java.lang.RuntimeException
import java.net.URL

object RoutesManager {
    lateinit var app: Application

    val canicaPath = "images/canica.png"

    enum class Views(val path: String) {
        GAME("views/game-view.fxml"),
        INSTRUCCIONES("views/instrucciones-view.fxml"),
        ACERCA_DE("views/acerca-de-view.fxml")
    }

    fun initGameStage(stage: Stage) {
        val fxmlLoader = FXMLLoader(app::class.java.getResource(Views.GAME.path))
        val scene = Scene(fxmlLoader.load(), 320.0, 460.0)
        stage.title = "Juego de las Canicas"
        stage.icons.add(Image(this.getResource(canicaPath).toExternalForm()))
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun initInstruccionesStage() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(app::class.java.getResource(Views.INSTRUCCIONES.path))
        val scene = Scene(fxmlLoader.load(), 530.0, 300.0)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Instrucciones - Juego de las Canicas"
        stage.icons.add(Image(this.getResource(canicaPath).toExternalForm()))
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun initAcercaDeStage() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(app::class.java.getResource(Views.ACERCA_DE.path))
        val scene = Scene(fxmlLoader.load(), 500.0, 200.0)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.title = "Acerca de - Juego de las Canicas"
        stage.icons.add(Image(this.getResource(canicaPath).toExternalForm()))
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }

    fun getResourceAsStream(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
           ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }
}
