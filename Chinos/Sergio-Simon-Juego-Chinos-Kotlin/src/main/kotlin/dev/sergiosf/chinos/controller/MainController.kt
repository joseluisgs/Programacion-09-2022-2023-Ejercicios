package dev.sergiosf.chinos.controller

import dev.sergiosf.chinos.routes.RoutersManager
import dev.sergiosf.chinos.viewmodels.ChinosViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainController: KoinComponent {

    val viewModel: ChinosViewModel by inject()

    @FXML
    lateinit var initParams: HBox

    @FXML
    lateinit var empatesCount: Text

    @FXML
    lateinit var iaCount: Text

    @FXML
    lateinit var pjCount: Text

    @FXML
    lateinit var countPlayers: HBox

    @FXML
    lateinit var tryRound: Button

    @FXML
    lateinit var enemyBox: VBox

    @FXML
    lateinit var nBolasEnemy: Spinner<Int>

    @FXML
    lateinit var intRoud: Text

    @FXML
    lateinit var stringRound: Text

    @FXML
    lateinit var nRondas: Spinner<Int>

    @FXML
    lateinit var nBolas: Spinner<Int>

    @FXML
    lateinit var start: Button

    @FXML
    lateinit var aboutButtom: MenuItem

    @FXML
    lateinit var exit: MenuItem

    private var bolasIA: Int = (1..7).random()

    private var rondas: Int = 0

    var iaPuntos: Int = 0
    var playerPuntos: Int = 0
    var empate: Int = 0

    @FXML
    private fun initialize() {

        initBindings()

        eventos()
    }

    private fun initBindings() {
//        nRondas.valueFactory.valueProperty().bindBidirectional(rondas)
    }

    private fun eventos() {
        // Menu
        aboutButtom.setOnAction { onAboutAction() }
        exit.setOnAction { onExitAction() }

        // Jugar
        start.setOnAction { onStartAction() }
        // Apostar
        tryRound.setOnAction { onRoundAction() }

        nBolas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 1, 2)
        nBolasEnemy.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(2, 14, 1)
        nRondas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1)
        enemyBox.isVisible = false
        //countPlayers.isVisible = false

        if (bolasIA % 2 == 0) bolasIA - 1
    }

    private fun onRoundAction() {
        rondas -= 1

        intRoud.text = rondas.toString()

        var intentoIA = (2..14).random()
//        var intentoIA = (1..6).random()
//        if (intentoIA % 2 == 0) intentoIA += 1


        val numeroTotalBolas = bolasIA + nBolas.value

        val ganadorPJ: Boolean = numeroTotalBolas == nBolasEnemy.value
        val ganadorIA: Boolean = numeroTotalBolas == intentoIA

//        val ganadorPJ: Boolean = nBolasEnemy.value == bolasIA
//        val ganadorIA: Boolean = nBolas.value == intentoIA

        when{
            ganadorIA && ganadorPJ -> {
                empate += 1
                rondas = 0
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Empate"
                    headerText = "Los dos jugadores habéis adivinado el mismo número "
                    contentText = "Ha ocurrido un empate siguiente ronda"
                }.showAndWait()
            }
            !ganadorIA && !ganadorPJ -> {
                empate += 1
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Empate"
                    headerText = "Ninguno de los dos jugadores habéis adivinado número de bolas del contrincante"
                    contentText = "Ha ocurrido un empate siguiente ronda"
                }.showAndWait()
            }
            ganadorIA -> {
                iaPuntos += 1
                rondas = 0
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Has perdido"
                    headerText = "El ordenador ha ganado"
                    contentText = "Has perdido :("
                }.showAndWait()
            }
            ganadorPJ -> {
                playerPuntos += 1
                rondas = 0
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Has ganado"
                    headerText = "✨ Has ganado ✨"
                    contentText = "Enhorabuena has ganado"
                }.showAndWait()
            }
        }

        if(rondas == 0) resetStage()

        iaCount.text = (iaPuntos).toString()
        pjCount.text = (playerPuntos).toString()
        empatesCount.text = (empate).toString()
        println("${nBolasEnemy.value} + ${bolasIA}")
    }

    private fun resetStage() {
        initParams.isVisible = true
        enemyBox.isVisible = false
        start.isVisible = true
        intRoud.text = "0"
//        countPlayers.isVisible = false
//        iaCount.text = "0"
//        pjCount.text = "0"
//        empatesCount.text = "0"
//        iaPuntos = 0
//        playerPuntos = 0
//        empate = 0
    }

    private fun onStartAction() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Start"
        alert.contentText = "Va a comenzar el juego ¿Desea continuar?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            enemyBox.isVisible = true
//            countPlayers.isVisible = true
            start.isVisible = false
            initParams.isVisible = false
            intRoud.text = nRondas.value.toString()
            rondas = nRondas.value
            if (bolasIA % 2 == 0) bolasIA += 1
        } else {
            alert.close()
        }
    }

    private fun onExitAction() {
        Alert(Alert.AlertType.WARNING)
            .apply {
                title = "Atención"
                headerText = "Se va a cerrar la aplicación"
                contentText = "Seguro que quieres cerrar la aplicación"
            }.showAndWait()
        System.exit(0)
    }

    private fun onAboutAction() {
        RoutersManager.initAboutStage()
    }
}