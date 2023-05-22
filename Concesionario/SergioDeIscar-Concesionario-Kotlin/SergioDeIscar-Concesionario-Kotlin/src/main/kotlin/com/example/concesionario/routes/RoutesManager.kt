package com.example.javafxdemo.routes

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.InputStream
import java.net.URL

object RoutesManager {
    lateinit var app: Application

    private val screenMap = mutableMapOf<Views, Scene>()
    private lateinit var stage: Stage

    fun setStage(stage: Stage){
        stage.setOnCloseRequest {
            exitAlert()
            it.consume()
        }

        this.stage = stage
    }

    fun addScreen(
        views: Views,
        widths: Double,
        heights: Double
    ) {
        screenMap[views] = Scene(
            FXMLLoader.load(getResource(views.fxml)),
            widths,
            heights
        )
    }

    fun removeScreen(view: Views) {
        screenMap.remove(view)
    }

    fun changeScene(
        view: Views,
        title: String,
        urlIcon: String = "images/icon.png"
    ) {
        val scene = screenMap[view] ?: return
        stage.title = title
        stage.scene = scene
        stage.icons.add(
            Image(
                this.getResourceAsStream(urlIcon)
            )
        )
        stage.isResizable = false
        stage.show()
    }

    fun openModal(
        view: Views,
        title: String,
        urlIcon: String = "images/icon.png"
    ) {
        val scene = screenMap[view] ?: return
        Stage().apply {
            this.title = title
            this.scene = scene
            this.icons.add(
                Image(
                    getResourceAsStream(urlIcon)
                )
            )
            this.initOwner(stage)
            this.initModality(Modality.WINDOW_MODAL)
            this.isResizable = false
        }.show()
    }

    fun exitAlert(){
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = "¿Estás seguro que quieres salir?"
            contentText = "Si sales perderás los datos de la partida"
        }.showAndWait().filter { it == ButtonType.OK }.ifPresent { Platform.exit() }
    }

    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("Resource $resource not found")
    }

    fun getResourceAsStream(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("Resource $resource not found")
    }
}