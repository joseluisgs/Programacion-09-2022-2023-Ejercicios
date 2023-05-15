package com.jczhang.jczhangbolaschinaskotlinjavafx.routes

import com.jczhang.jczhangbolaschinaskotlinjavafx.controllers.MainViewController
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.awt.Image
import java.io.File
import java.net.URL
import java.util.*


private val logger = KotlinLogging.logger{}


object RouterManager {
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage

    val activeStage: Stage
        get() = _activeStage

    lateinit var  app: Application

    enum class Views(val fxml: String){
        MAIN("main-view.fxml"),
        ACERCA_DE("acercaDeView.fxml")
    }

    init {
        logger.debug { "Initializing route manager" }
        Locale.setDefault(Locale("es","ES"))
    }


    fun mainStageInit(stage: Stage){
        logger.debug { "Iniciando MainStage" }

        val controller = MainViewController()

        val fxmlloader = FXMLLoader(getResource(Views.MAIN.fxml))
        val parentRoot = fxmlloader.load<Pane>()
        val scene = Scene(parentRoot, 600.0, 550.0)
        stage.title = "Adivina mis bolas"
        stage.icons.add(javafx.scene.image.Image("${System.getProperty("user.dir")}${File.separator}src${File.separator}main${File.separator}resources${File.separator}icons${File.separator}canicas.png"))
        stage.scene = scene
        stage.onCloseRequest = EventHandler { event ->
            controller.onCloseAction(event)
        }
        mainStage = stage
        _activeStage = stage

        stage.isResizable = true
        mainStage.show()

    }


    fun acercaDeInit(){
        logger.debug { "Iniciando Acerca de " }

        val fxmlLoader = FXMLLoader(getResource(Views.ACERCA_DE.fxml ))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 500.0, 250.0)

        val stage = Stage()
        stage.title = "Acerca de"
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    private fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)?: throw RuntimeException("No se ha encontrado el recurso $resource")
    }

}