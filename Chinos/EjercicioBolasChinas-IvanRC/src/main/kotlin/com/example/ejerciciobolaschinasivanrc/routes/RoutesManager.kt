package com.example.ejerciciobolaschinasivanrc.routes

import com.example.ejerciciobolaschinasivanrc.controllers.MainController
import com.example.ejerciciobolaschinasivanrc.utils.getResource
import com.example.ejerciciobolaschinasivanrc.utils.getResourceAsStream
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {  }

object RoutesManager {
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    private val activeStage get() = _activeStage
    lateinit var app: Application

    init {
        logger.debug { "Se inicia el RoutesManager" }
        Locale.setDefault(Locale("es", "ES"))
    }


    enum class views(val path: String){
        MAIN("views/main-view.fxml"),
        MODAL("views/modal-view.fxml")
    }

    fun initMainStage(stage: Stage){
        logger.debug { "Se inicia el stage principal de la app" }
        val fxmlLoader = FXMLLoader(getResource(views.MAIN.path))
        val scene = Scene(fxmlLoader.load(), 500.0, 680.0)
        stage.apply {
            title = "Juego de las bolas chinas"
            isResizable = false
            icons.add(Image(getResourceAsStream("icons/bolas.png")))
            setOnCloseRequest {
                fxmlLoader.getController<MainController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        mainStage = stage
        _activeStage = stage

        mainStage.show()
    }

    fun initModelStage(){
        logger.debug { "Se inicia la stage modal de la app" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(views.MODAL.path))
        val scene = Scene(fxmlLoader.load(), 600.0, 340.0)
        stage.title = "Información"
        stage.isResizable = false
        stage.initStyle(StageStyle.DECORATED)
        stage.icons.add(Image(getResourceAsStream("icons/bolas.png")))
        stage.scene = scene

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage
        activeStage.show()
    }
}