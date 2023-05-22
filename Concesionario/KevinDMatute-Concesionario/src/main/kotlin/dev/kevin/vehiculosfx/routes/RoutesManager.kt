package dev.kevin.vehiculosfx.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.io.InputStream
import java.lang.RuntimeException
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

private val logger = KotlinLogging.logger {  }

object RoutesManager {

    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    val activeStage: Stage
        get() = _activeStage
    lateinit var app: Application

    private var sceneMap: HashMap<String, Pane> = HashMap()

    enum class View(val fxml: String) {
        MAIN("hello-view.fxml"), DETAILS("details-view.fxml")

    }

    init {

        Locale.setDefault(Locale("es", "ES"))
    }

    fun initMainStage(stage: Stage){
        logger.debug { "Iniciando Main Stage" }
        val fxmlLoader = FXMLLoader(getResource(View.MAIN.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 700.0, 450.0)

        stage.title = "Vehiculos"
        stage.isResizable = false

        stage.scene = scene
        mainStage = stage
        _activeStage = stage
        mainStage.show()

    }

    fun initDetalle(){
        logger.debug { "Iniciando Detalles Stage" }
        val stage = Stage()

        val fxmlLoader = FXMLLoader(getResource(View.DETAILS.fxml))
        //val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(fxmlLoader.load(), 419.0, 424.0)
        stage.title = "Detalles del vehiculo"
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

    fun gerResourcesAsStream(resource: String): InputStream{
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
    }
}