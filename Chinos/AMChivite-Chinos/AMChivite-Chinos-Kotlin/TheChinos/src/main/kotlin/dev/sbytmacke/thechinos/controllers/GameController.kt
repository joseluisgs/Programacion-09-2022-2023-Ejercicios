package dev.sbytmacke.thechinos.controllers

import dev.sbytmacke.thechinos.MainApp
import dev.sbytmacke.thechinos.routes.RoutesManager
import dev.sbytmacke.thechinos.states.AppState
import dev.sbytmacke.thechinos.viewmodels.BallGameViewModel
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class GameController {

    @FXML
    private lateinit var iaPoints: Label

    @FXML
    private lateinit var playerPoints: Label

    @FXML
    private lateinit var ballGameViewModel: BallGameViewModel

    @FXML
    private lateinit var newGame: MenuItem

    @FXML
    private lateinit var roundCounterLabel: Label

    @FXML
    private lateinit var finalResultLabel: Label

    @FXML
    private lateinit var checkButton: Button

    @FXML
    private lateinit var ballsPlayerPosesion: Spinner<Any>

    @FXML
    private lateinit var spinnerBallsToGuess: Spinner<Any>

    @FXML
    private lateinit var guideMenuItem: MenuItem

    @FXML
    private lateinit var exitMenuItem: MenuItem

    @FXML
    private lateinit var aboutMenuItem: MenuItem

    @FXML
    private lateinit var chinoImage: ImageView

    @FXML
    fun initialize() {
        // Supone acoplamiento, debo utilizarlo como objecto o inyectar por parámetro:
        ballGameViewModel = BallGameViewModel()

        // Inicializar spinnerBallsToGuess, SE PUEDE HACER DE OTRA MANERA???
        spinnerBallsToGuess = Spinner(3, 7, 3)

        // Acciones del menú
        exitMenuItem.setOnAction { onExitButtonClick() }
        aboutMenuItem.setOnAction { onAcercaDeButtonClick() }
        guideMenuItem.setOnAction { onGuideButtonClick() }
        newGame.setOnAction { onStartNewGameButtonClick() }

        // Imagen estática
        chinoImage.image = Image(MainApp::class.java.getResourceAsStream("/icons/canicas.png"))

        // Acciones botones
        checkButton.setOnAction { onComprobarButtonClick() }

        // Reactividad
        iaPoints.textProperty().bind(AppState.countIAWins.asString())
        playerPoints.textProperty().bind(AppState.countPlayerWins.asString())
    }

    private fun onComprobarButtonClick() {

        val resultCheck =
            ballGameViewModel.isAcertador(spinnerBallsToGuess.value as Int, ballsPlayerPosesion.value as Int)
        finalResultLabel.text = resultCheck.message()

        if (AppState.countIAWins.value == AppState.rounds) {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Resultado final"
            alert.contentText = "¡Ha ganado la IA en la ronda ${roundCounterLabel.text}!"
            alert.showAndWait()
            RoutesManager.initMainStage(RoutesManager.getMainStage())
        }
        if (AppState.countPlayerWins.value == AppState.rounds) {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Resultado final"
            alert.contentText = "¡Has ganado en la ronda ${roundCounterLabel.text}!"
            alert.showAndWait()
            RoutesManager.initMainStage(RoutesManager.getMainStage())
        }

        // Siguiente ronda
        roundCounterLabel.text = (roundCounterLabel.text.toInt() + 1).toString()
    }

    private fun onStartNewGameButtonClick() {
        RoutesManager.initMainStage(RoutesManager.getMainStage())
    }

    private fun onGuideButtonClick() {
        RoutesManager.initGuideGame()
    }

    private fun onExitButtonClick() {
        Platform.exit()
    }

    private fun onAcercaDeButtonClick() {
        RoutesManager.initAcercaDeStage()
    }
}