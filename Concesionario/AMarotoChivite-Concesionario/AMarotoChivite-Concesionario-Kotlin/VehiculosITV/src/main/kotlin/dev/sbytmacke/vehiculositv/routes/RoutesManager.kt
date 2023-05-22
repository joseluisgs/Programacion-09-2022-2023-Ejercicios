package dev.sbytmacke.vehiculositv.routes

import dev.sbytmacke.vehiculositv.MainApp
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object RoutesManager {

    private lateinit var _mainStage: Stage
    private lateinit var _activeStage: Stage

    fun getActiveStage(): Stage {
        return _activeStage
    }

    // Definimos las rutas de las vistas que tengamos
    enum class TypeView(val pathFxml: String) {
        MAIN("views/main-view.fxml"),
        ACERCA_DE("views/acerca-view.fxml"),
        DETAILS_VEHICULE("views/details-view.fxml"),
    }

    init {
        logger.debug { "Inicializando..." }
    }

    fun initMainStage(stage: Stage) {
        logger.debug { "Inicializando MainStage" }

        // Cargamos el fxml para poder leerlo del resources (es estático)
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource(TypeView.MAIN.pathFxml))

        // Pasar instancia del controlador al FXMLLoader
        //fxmlLoader.setController(mainVieController)

        // Configuración de la escena
        val scene = Scene(fxmlLoader.load(), 808.0, 486.0)
        stage.title = "ITV"
        stage.isResizable = false

        // Añadir icono desde los recursos
        val iconStream = MainApp::class.java.getResourceAsStream("icons/about.png")
        val icon = Image(iconStream)
        stage.icons.add(icon)

        // Control de Stages y Escenas
        stage.scene = scene
        _mainStage = stage
        _activeStage = stage

        // Mostrarla
        _mainStage.show()
    }

    // Será ventana nueva Modal
    fun initAcercaDeStage() {
        logger.debug { "Inicializando AcercaDeStage" }

        // Cargamos el fxml para poder leerlo del resources (es estático)
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource(TypeView.ACERCA_DE.pathFxml))

        // Configuración de la escena
        val scene = Scene(fxmlLoader.load(), 259.0, 127.0)
        // Es nueva ventana = nueva stage
        val stage = Stage()
        stage.title = "Acerca De ITV"
        stage.isResizable = false

        // Añadir icono desde los recursos
        val iconStream = MainApp::class.java.getResourceAsStream("icons/about.png")
        val icon = Image(iconStream)
        stage.icons.add(icon)

        // Control de Stages y Escenas
        stage.scene = scene
        stage.initOwner(_mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        // Mostrarla
        stage.show()
    }

    // Será ventana nueva Modal
    fun initDetailsVehiculeStage() {
        logger.debug { "Inicializando Detalle De Vehiculos" }

        // Cargamos el fxml para poder leerlo del resources (es estático)
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource(TypeView.DETAILS_VEHICULE.pathFxml))

        // Configuración de la escena
        val scene = Scene(fxmlLoader.load(), 350.0, 400.0)
        // Es nueva ventana = nueva stage
        val stage = Stage()
        stage.title = "Detalle de Vehiculo"
        stage.isResizable = false

        // Añadir icono desde los recursos
        val iconStream = MainApp::class.java.getResourceAsStream("icons/about.png")
        val icon = Image(iconStream)
        stage.icons.add(icon)

        // Control de Stages y Escenas
        stage.scene = scene
        stage.initOwner(_mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        // Mostrarla
        stage.show()
    }
}

