package es.sergiomisas.juegodelasbolas.controllers

import es.sergiomisas.juegodelasbolas.data.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.ImageView

class BolasController {


    @FXML
    private lateinit var menuItemAcercaDe: MenuItem

    @FXML
    private lateinit var labelRondas: Label

    @FXML
    private lateinit var labelVictoriasJugador: Label

    @FXML
    private lateinit var labelVictoriasOrdenador: Label

    @FXML
    private lateinit var labelEmpates: Label

    @FXML
    private lateinit var imageViewTalisca: ImageView

    @FXML
    lateinit var labelPregunta: Label

    @FXML
    private lateinit var spinnerCanicas: Spinner<Int>

    @FXML
    private lateinit var buttonSeleccionar: Button

    @FXML
    lateinit var buttonResetearJuego: Button

    @FXML
    private fun initialize() {
        labelRondas.text = "0"
        labelVictoriasJugador.text = "0"
        labelVictoriasOrdenador.text = "0"
        labelEmpates.text = "0"
        buttonResetearJuego.isVisible = false
    }

    @FXML
    private fun onClickSeleccionar() {
        val ordenadorDecision = listOf(3, 5, 7).random()
        val bolas = generarBolas()

        if (ordenadorDecision == bolas.first() && spinnerCanicas.value == bolas.last()) {
            labelEmpates.text = (labelEmpates.text.toInt() + 1).toString()
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Empate"
            alert.headerText = "Empate"
            alert.contentText = "Se ha empatado contra el ordenador"
            alert.showAndWait()
        } else if (ordenadorDecision != bolas.first() && spinnerCanicas.value == bolas.last()) {
            labelVictoriasJugador.text = (labelVictoriasJugador.text.toInt() + 1).toString()
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Victoria"
            alert.headerText = "Victoria"
            alert.contentText = "Se ha ganado contra el ordenador"
            alert.showAndWait()
        } else {
            labelVictoriasOrdenador.text = (labelVictoriasOrdenador.text.toInt() + 1).toString()
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Derrota"
            alert.headerText = "Derrota"
            alert.contentText = "Se ha perdido contra el ordenador"
            alert.showAndWait()
        }

        labelRondas.text = (labelRondas.text.toInt() + 1).toString()

        if (labelRondas.text.toInt() == 3) generarResultado()
    }

    private fun generarBolas(): List<Int> {
        val bolasJugador = listOf(3, 5, 7).random()
        val bolasOrdenador = listOf(3, 5, 7).random()
        return listOf(bolasJugador, bolasOrdenador)
    }

    private fun generarResultado() {
        when {
            labelVictoriasJugador.text.toInt() > labelVictoriasOrdenador.text.toInt() -> {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Victoria Partida"
                alert.headerText = "Winner winner chicken dinner"
                alert.contentText = "GANASTE!!!!!"
                alert.showAndWait()

            }

            labelVictoriasJugador.text.toInt() < labelVictoriasOrdenador.text.toInt() -> {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Derrota Partida"
                alert.headerText = "Perdiste la partida :("
                alert.contentText = "YOU FAILED!!!"
                alert.showAndWait()
            }

            else -> {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Empate Partida"
                alert.headerText = "Se ha empatado"
                alert.contentText = "EMPATE!!!!!"
                alert.showAndWait()
            }
        }

        buttonResetearJuego.isVisible = true
        labelPregunta.isVisible = false
        spinnerCanicas.isVisible = false
        buttonSeleccionar.isVisible = false
    }

    @FXML
    private fun onClickResetearJuego() {
        buttonResetearJuego.isVisible = false
        labelPregunta.isVisible = true
        spinnerCanicas.isVisible = true
        buttonSeleccionar.isVisible = true
        labelRondas.text = "0"
        labelVictoriasJugador.text = "0"
        labelVictoriasOrdenador.text = "0"
        labelEmpates.text = "0"
    }

    @FXML
    private fun onClickAcercaDe() {
        RoutesManager.initAcercaDeStage()
    }

}
