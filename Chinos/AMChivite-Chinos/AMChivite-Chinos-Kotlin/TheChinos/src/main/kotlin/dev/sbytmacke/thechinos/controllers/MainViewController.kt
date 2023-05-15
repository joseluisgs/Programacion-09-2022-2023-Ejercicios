package dev.sbytmacke.thechinos.controllers

import dev.sbytmacke.thechinos.MainApp
import dev.sbytmacke.thechinos.routes.RoutesManager
import dev.sbytmacke.thechinos.states.AppState
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.scene.control.Spinner
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class MainViewController {

    @FXML
    private lateinit var newGame: MenuItem

    @FXML
    private lateinit var guideMenuItem: MenuItem

    @FXML
    private lateinit var exitMenuItem: MenuItem

    @FXML
    private lateinit var aboutMenuItem: MenuItem

    @FXML
    private lateinit var chinoImage: ImageView

    @FXML
    private lateinit var startButton: Button

    @FXML
    private lateinit var roundsSpinner: Spinner<Any>

    @FXML
    fun initialize() {
        // Acciones del menú
        exitMenuItem.setOnAction { onExitButtonClick() }
        aboutMenuItem.setOnAction { onAcercaDeButtonClick() }
        guideMenuItem.setOnAction { onGuideButtonClick() }
        newGame.setOnAction { onStartNewGameButtonClick() }

        // Imagen estática
        chinoImage.image = Image(MainApp::class.java.getResourceAsStream("/icons/chino.png"))

        // Acciones del botón
        startButton.setOnAction { onStartButtonClick() }
    }

    private fun onStartNewGameButtonClick() {
        RoutesManager.initMainStage(RoutesManager.getMainStage())
    }

    private fun onGuideButtonClick() {
        RoutesManager.initGuideGame()
    }

    private fun onStartButtonClick() {
        // Guardo la elección de cuantas rondas jugaremos en mi State
        AppState.rounds = roundsSpinner.value as Int

        // Navegamos para iniciar el juego
        RoutesManager.initGame()
    }

    private fun onExitButtonClick() {
        Platform.exit()
    }

    private fun onAcercaDeButtonClick() {
        RoutesManager.initAcercaDeStage()
    }

}