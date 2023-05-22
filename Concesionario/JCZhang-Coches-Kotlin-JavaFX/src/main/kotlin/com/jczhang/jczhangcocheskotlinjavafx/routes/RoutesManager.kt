package com.jczhang.jczhangcocheskotlinjavafx.routes

import com.jczhang.jczhangcocheskotlinjavafx.controller.CochesViewController
import com.jczhang.jczhangcocheskotlinjavafx.controller.EditItemViewController
import com.jczhang.jczhangcocheskotlinjavafx.viewmodel.CochesViewModel
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URL
import java.util.*

private val logger = KotlinLogging.logger {}

object RoutesManager: KoinComponent {

    private val viewModel: CochesViewModel by inject()
    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage

    val activeStage: Stage
        get() = _activeStage
    lateinit var app: Application

    enum class  View(val fxml: String){
        MAIN("Coches-View.fxml"),
        ACERCA_DE("acercaDe-View.fxml"),
        ADD_ITEM("addItem-view.fxml"),
        EDIT_ITEM("editItem-View.fxml")
    }

    init{
        logger.debug { "Iniciando routes manager" }
        //Defino el idioma predeterminado como español
        Locale.setDefault(Locale("es","ES"))
    }
    private val mainController = CochesViewController()

    fun initMainStage(stage: Stage){
        logger.debug { "Iniciando Main Stage"}
        val fxmlLoader = FXMLLoader(getResource(View.MAIN.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 680.0, 440.0)
        stage.title = "Coches"
        stage.isResizable = false
        stage.onCloseRequest = EventHandler { event ->
            mainController.onCloseAction(event)
        }
        stage.scene = scene
        mainStage = stage
        _activeStage = stage
        mainStage.show()
    }

    fun initAcercaDeStage() {
        logger.debug { "Iniciando acerca de" }

        val fxmlLoader = FXMLLoader(getResource(View.ACERCA_DE.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 450.0, 155.0)
        val stage = Stage()
        stage.title = "Acerca de..."
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        stage.show()
    }

    fun initAddItemStage(){
        logger.debug { "Iniciando AddItem Stage" }
        val fxmlLoader = FXMLLoader(getResource(View.ADD_ITEM.fxml))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 310.0, 410.0)
        val stage = Stage()
        stage.title = "Añadir Coche"
        stage.scene = scene
        _activeStage = stage
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initEditItemStage() {
        logger.debug { "Iniciando EditItem Stage" }

        val fxmlLoader = FXMLLoader()
        fxmlLoader.location = getResource(View.EDIT_ITEM.fxml)
        val parentRoot: Pane = fxmlLoader.load()
        val controller: EditItemViewController = fxmlLoader.getController()

        val scene = Scene(parentRoot, 310.0, 410.0)
        val stage = Stage()

        stage.title = "Editar Coche"
        stage.scene = scene
        _activeStage = stage
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        // Llama al método showData() del controlador después de cargar la vista
        stage.setOnShown {
            controller.showData()
        }

        stage.show()
    }



    private fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)?: throw RuntimeException("No se ha encontrado el recurso deseado")
    }

}

