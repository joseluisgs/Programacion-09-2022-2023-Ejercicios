package com.example.gestionvehiculosconimagenes_kotlin.routes

import com.example.gestionvehiculosconimagenes_kotlin.controllers.VehiculosController
import com.example.gestionvehiculosconimagenes_kotlin.controllers.VehiculosDetallesController
import com.example.gestionvehiculosconimagenes_kotlin.utils.getResource
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.util.Locale

private val logger = KotlinLogging.logger {  }

object RoutesManager {
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    val activeStage get() = _activeStage
    lateinit var app: Application

    init {
        logger.debug { "Se inicia el routes manager" }
        Locale.setDefault(Locale("es", "ES"))
    }

    private enum class Vistas(val url: String){
        MAIN("views/main-view-vehiculos.fxml"),
        DETALLE_VEHICULO("views/detalles-view-vehiculo.fxml"),
        MODAL("views/modal-view.fxml")
    }

    fun initMainStage(stage: Stage){
        logger.debug { "Se inicia la vista principal de la aplicación" }
        val fxmlLoader = FXMLLoader(getResource(Vistas.MAIN.url))
        val scene = Scene(fxmlLoader.load(), 650.0, 430.0)
        stage.apply {
            title = "Gestión de vehículos"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<VehiculosController>().onCloseActionClick(it)
            }
            stage.scene = scene
        }

        mainStage = stage
        _activeStage = stage

        mainStage.show()
    }

    fun initDetalleViewVehiculo(){
        logger.debug { "Se inicia la vista detalles de vehiculos de la aplicación" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.DETALLE_VEHICULO.url))
        val scene = Scene(fxmlLoader.load(), 270.0, 400.0)
        stage.apply {
            title = "Gestión de vehículos"
            isResizable = false
            setOnCloseRequest {
                fxmlLoader.getController<VehiculosDetallesController>().onClickCancelarOperacion(it)
            }
            stage.scene = scene
        }

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage

        activeStage.show()
    }

    fun initModalView(){
        logger.debug { "Se inicia la ventana modal del Acerca De" }
        val stage = Stage()
        val fxmlLoader = FXMLLoader(getResource(Vistas.MODAL.url))
        val scene = Scene(fxmlLoader.load(), 600.0, 415.0)
        stage.apply {
            title = "Acerca de la aplicación de gestión de vehículos"
            isResizable = false
            stage.scene = scene
        }

        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        _activeStage = stage
        activeStage.show()
    }
}