package es.sergiomisas.juegodelasbolas.data.routes

import es.sergiomisas.juegodelasbolas.HelloApplication
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.InputStream
import java.net.URL

object RoutesManager {

    lateinit var app: Application

    fun initMainStage(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("bolas-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 520.0, 480.0)
        stage.title = "Juego de las bolas"
        stage.scene = scene
        stage.show()
    }

    fun initAcercaDeStage(){
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("acerca-de-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 520.0, 480.0)
        val stage = Stage()
        stage.initModality(Modality.WINDOW_MODAL)
        stage.title = "Acerca de "
        stage.scene = scene
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
