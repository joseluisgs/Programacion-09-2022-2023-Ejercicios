package com.example.vehiculoscrudinterface.routes

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.controllers.AgregarViewController
import com.example.vehiculoscrudinterface.controllers.BorrarViewController
import com.example.vehiculoscrudinterface.controllers.ConcesionarioController
import com.example.vehiculoscrudinterface.controllers.EditarViewController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.util.Collections

private val logger = KotlinLogging.logger{}

object RoutesManager {

    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage
    val activeStage get() = _activeStage
    lateinit var app: Application
    private val iconoApp = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/iconoApp.png"))
    private val iconoBorrar = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/iconoBorrar.png"))
    private val iconoEditar = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/iconoEditar.png"))
    private val iconoAgregar = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/iconoAgregar.png"))
    private val controladorPrincipal = ConcesionarioController()

    fun initMainStage(stage: Stage) {
        logger.debug { "RoutesManager -> Iniciando Stage Principal" }
        val fxmlLoader = FXMLLoader(ConcesionarioApplication::class.java.getResource("views/vista-principal.fxml"))

        fxmlLoader.setController(controladorPrincipal)
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        stage.title = "Concesionario"
        stage.scene = scene
        stage.isResizable = false
        mainStage = stage
        _activeStage = stage
        stage.icons.add(iconoApp)
        stage.show()
    }

    fun borrarStage(id: String) {
        logger.debug { "RoutesManager -> Iniciando Stage Borrar" }
        val fxmlLoader = FXMLLoader(ConcesionarioApplication::class.java.getResource("views/vista-borrar.fxml"))
        val controlador = BorrarViewController(id)
        fxmlLoader.setController(controlador)
        val scene = Scene(fxmlLoader.load(), 357.0, 373.0)
        val stage = Stage()
        stage.title = "Borrar Vehículo"
        stage.scene = scene
        stage.isResizable = false
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.icons.add(iconoBorrar)
        stage.setOnHidden {
            controladorPrincipal.actualizarLista()
        }
        stage.showAndWait()
    }

    fun editarStage(id: String) {
        logger.debug { "RoutesManager -> Iniciando Stage Editar" }
        val fxmlLoader = FXMLLoader(ConcesionarioApplication::class.java.getResource("views/vista-editar.fxml"))
        val controlador = EditarViewController(id)
        fxmlLoader.setController(controlador)
        val scene = Scene(fxmlLoader.load(), 357.0, 373.0)
        val stage = Stage()
        stage.title = "Editar Vehículo"
        stage.scene = scene
        stage.isResizable = false
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.icons.add(iconoEditar)
        stage.setOnHidden {
            controladorPrincipal.actualizarLista()
        }
        stage.showAndWait()
    }

    fun agregarStage() {
        logger.debug { "RoutesManager -> Iniciando Stage Agregar" }
        val fxmlLoader = FXMLLoader(ConcesionarioApplication::class.java.getResource("views/vista-agregar.fxml"))
        val controlador = AgregarViewController()
        fxmlLoader.setController(controlador)
        val scene = Scene(fxmlLoader.load(), 357.0, 373.0)
        val stage = Stage()
        stage.title = "Agregar Vehículo"
        stage.scene = scene
        stage.isResizable = false
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.icons.add(iconoAgregar)
        stage.setOnHidden {
            controladorPrincipal.actualizarLista()
        }
        stage.showAndWait()
    }
}