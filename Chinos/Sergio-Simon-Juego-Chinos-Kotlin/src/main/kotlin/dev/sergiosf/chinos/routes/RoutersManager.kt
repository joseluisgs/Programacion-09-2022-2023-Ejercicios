package dev.sergiosf.chinos.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.InputStream
import java.net.URL

object RoutersManager {

    private lateinit var mainStage: Stage // La ventana principal
    private lateinit var _activeStage: Stage // La ventana actual

    val activeStage: Stage
        get() = _activeStage


    lateinit var app: Application

    private var sceneMap: HashMap<String, Pane> = HashMap()

    enum class View(val fxml: String) {
        MAIN("view/main-view.fxml"),
        ABOUT("view/about-view.fxml"),
    }

    fun initMainStage(stage: Stage) {
        val fxml = FXMLLoader(this.getResource(View.MAIN.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 600.0, 400.0)
        stage.title = "Juego de los chinos"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream("images/chinese.png")))
        stage.setOnCloseRequest {
            Alert(Alert.AlertType.WARNING)
                .apply {
                    title = "Atención"
                    headerText = "Se va a cerrar la aplicación"
                    contentText = "Seguro que quieres cerrar la aplicación"
                }.showAndWait()
        }
        stage.scene = scene
        mainStage = stage
        _activeStage = stage
        mainStage.show()
    }

    fun initAboutStage() {
        val fxml = FXMLLoader(this.getResource(View.ABOUT.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 329.0, 155.0)
        val stage = Stage()
        stage.title = "Juego de los chinos"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream("images/chinese.png")))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }


    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }

    fun getResourceAsStream(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
    }
}