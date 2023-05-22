package dev.sergiosf.concesionario.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.io.InputStream
import java.net.URL
import java.util.*

private val logger = KotlinLogging.logger{}

object RoutesManager {
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage

    val activeStage: Stage
        get() = _activeStage

    lateinit var app: Application

    enum class View(val fxml: String) {
        MAIN("views/concesionario-view.fxml"),
        ABOUT("views/about-view.fxml"),
        DETAILS("views/detalle-view.fxml"),
    }

    init {
        Locale.setDefault(Locale("es", "ES"))
    }

    fun initMainStage(stage: Stage) {
        logger.debug { "Iniciando la vista principal" }

        val fxml = FXMLLoader(this.getResource(View.MAIN.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 680.0, 440.0)
        stage.title = "Concesionario"
        stage.icons.add(Image(getResourceAsStream("images/car-icon.png")))
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
        stage.title = "Concesionario"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream("images/car-icon.png")))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }

    fun initDetalle() {
        logger.debug { "Inicializando Detalle" }

        val fxmlLoader = FXMLLoader(getResource(View.DETAILS.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 350.0, 400.0)
        val stage = Stage()
        stage.title = "Detalle de Vehiculo"
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

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