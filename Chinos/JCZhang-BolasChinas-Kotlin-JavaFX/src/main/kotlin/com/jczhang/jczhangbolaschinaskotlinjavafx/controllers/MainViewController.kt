package com.jczhang.jczhangbolaschinaskotlinjavafx.controllers


import com.jczhang.jczhangbolaschinaskotlinjavafx.models.PC
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Spinner
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import mu.KotlinLogging
import com.jczhang.jczhangbolaschinaskotlinjavafx.routes.RouterManager
import javafx.application.Platform
import javafx.event.Event
import javafx.scene.control.SpinnerValueFactory
import java.util.concurrent.CountDownLatch

class MainViewController {

    @FXML
    lateinit var victorias: TextField

    @FXML
    lateinit var derrotas: TextField

    @FXML
    lateinit var textArea: TextArea

    @FXML
    lateinit var numRondas: Spinner<Int>

    @FXML
    lateinit var apuesta: TextField

    private val logger = KotlinLogging.logger {}


    private var victoriasCount = 0
    private var derrotasCount = 0

    private var numRondasActual = 0
    private var contadorRondas = 0

    @FXML
    fun initialize() {
        numRondas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1, 1)
    }

    fun onCloseAction(event: Event) {
        logger.debug { "Closing" }

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.apply {
            headerText = "¿Está seguro de que desea salir?"
            contentText = "Se perderán todos los datos no guardados."

        }.showAndWait().ifPresent{
            if (it == ButtonType.OK){
                Platform.exit()
            }else{
                event.consume()
            }
        }
    }

    fun onAcercaDeAction() {
        RouterManager.acercaDeInit()
    }

    @FXML
    fun onAceptarNumRondas() {
        numRondasActual = numRondas.value!!
        contadorRondas = 0
        iniciarPartida()
    }

    private fun iniciarPartida() {
        if (contadorRondas < numRondasActual) {
            contadorRondas++
            if (contadorRondas == 1) {
                escribirInstrucciones()
            }

        } else {
            textArea.appendText("La partida ha terminado \n")
        }
    }

    private fun escribirInstrucciones() {
        textArea.text = ""
        textArea.appendText("Bienvenido al juego de las bolas chinas \n")
        textArea.appendText("El ordenador ha elegido un número entre 0 y 5 \n")
        textArea.appendText("Introduce tu apuesta: \n")
        textArea.appendText("\n")
    }

    @FXML
    fun comprobarApuesta() {
        val numPc = PC().generarNumRandom()

        if (!validate(apuesta)) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.apply {
                title = "Error"
                contentText = "La apuesta introducida no es válida"
            }
            alert.showAndWait()
            return
        }

        val apuestaUsuario = apuesta.text.toInt()

        if (apuestaUsuario == numPc) {

            textArea.appendText("¡Enhorabuena! Has adivinado el número y has conseguido una victoria.\n")
            victoriasCount++
            victorias
            victorias.text = victoriasCount.toString()
        } else {
            textArea.appendText("Es una lástima, no has adivinado el número y has sufrido una derrota.\n")
            derrotasCount++
            derrotas.text = derrotasCount.toString()
        }

        if (contadorRondas < numRondasActual) {
            iniciarPartida()
        } else {
            textArea.appendText("La partida ha terminado \n")
        }
    }


    private fun validate(apuesta: TextField): Boolean {
        val regex = "[0-9]+".toRegex()
        return regex.matches(apuesta.text)
    }

}

