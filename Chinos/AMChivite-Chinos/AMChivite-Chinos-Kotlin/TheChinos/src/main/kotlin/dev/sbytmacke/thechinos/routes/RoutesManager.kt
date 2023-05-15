package dev.sbytmacke.thechinos.routes

import dev.sbytmacke.thechinos.MainApp
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Screen
import javafx.stage.Stage

object RoutesManager {
    private lateinit var _mainStage: Stage // La ventana principal
    private lateinit var _activeStage: Stage // La ventana actual

    fun getMainStage(): Stage {
        return _mainStage
    }

    fun initMainStage(stage: Stage) {
        // Cargamos el fxml para poder leerlo del resources (es estático)
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("main-view.fxml"))

        // Obtén el tamaño de la pantalla del usuario
        val screenBounds = Screen.getPrimary().visualBounds

        // Crea la escena y asigna el tamaño de pantalla al ancho y alto de la escena
        val mainEscene = Scene(fxmlLoader.load(), 329.0 /*screenBounds.width*/, 356.0 /*screenBounds.height - 25*/)

        stage.title = "TheChinos!"

        // Añadir icono desde los recursos
        val iconStream = MainApp::class.java.getResourceAsStream("/icons/chino.png")
        val icon = Image(iconStream)
        stage.icons.add(icon)

        // El _mainstage será único y nunca lo cambiaremos más, a partir de aquí jugamos con las escenas
        _mainStage = stage
        _activeStage = stage

        // Control de escenas
        _mainStage.scene = mainEscene
        _mainStage.show()
    }

    fun initAcercaDeStage() {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("acerca-de-view.fxml"))

        val acercaDeEscene = Scene(fxmlLoader.load(), 299.0, 162.0)

        val stage = Stage()
        stage.title = "Acerca De..."

        // Añadir icono desde los recursos
        val iconStream = MainApp::class.java.getResourceAsStream("/icons/info.png")
        val icon = Image(iconStream)
        stage.icons.add(icon)

        stage.scene = acercaDeEscene

        stage.initOwner(_mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        stage.show()
    }

    fun initGuideGame() {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("guide-game-view.fxml"))

        val guideGameEscene = Scene(fxmlLoader.load(), 500.0, 300.0)

        val stage = Stage()
        stage.title = "TheChinos!"

        stage.scene = guideGameEscene

        stage.initOwner(_mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        stage.show()
    }

    fun initGame() {
        val fxmlLoader = FXMLLoader(MainApp::class.java.getResource("game-view.fxml"))

        val gameScene = Scene(fxmlLoader.load())

        // Asignar la nueva escena a la STAGE principal
        _mainStage.scene = gameScene

        _mainStage.show()
    }
}