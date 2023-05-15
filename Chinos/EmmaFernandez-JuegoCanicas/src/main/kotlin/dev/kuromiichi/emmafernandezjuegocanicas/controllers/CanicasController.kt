package dev.kuromiichi.emmafernandezjuegocanicas.controllers

import dev.kuromiichi.emmafernandezjuegocanicas.Juego
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import javafx.scene.control.TextField

class CanicasController {
    @FXML
    private lateinit var textFieldGanadas: TextField

    @FXML
    private lateinit var textFieldPerdidas: TextField

    @FXML
    private lateinit var textFieldPuntosJugador: TextField

    @FXML
    private lateinit var textFieldPuntosCpu: TextField

    @FXML
    private lateinit var spinnerCanicas: Spinner<Int>

    @FXML
    private lateinit var buttonConfirmar: Button

    @FXML
    private fun initialize() {
        // Text fields no editables para representar valores
        textFieldGanadas.isEditable = false
        textFieldPerdidas.isEditable = false
        textFieldPuntosJugador.isEditable = false
        textFieldPuntosCpu.isEditable = false

        // Spinner de canicas con valores fijos (3, 5 o 7)
        spinnerCanicas.valueFactory = IntegerSpinnerValueFactory(3, 7, 3, 2)

        // Bindings
        textFieldGanadas.textProperty().bindBidirectional(Juego.rondasGanadas)
        textFieldPerdidas.textProperty().bindBidirectional(Juego.rondasPerdidas)
        textFieldPuntosJugador.textProperty().bindBidirectional(Juego.puntosJugador)
        textFieldPuntosCpu.textProperty().bindBidirectional(Juego.puntosCpu)

        // Botón de confirmar
        buttonConfirmar.setOnAction { onButtonConfirmarClick() }

        // iniciar ronda
        Juego.iniciarRonda()
    }

    @FXML
    private fun onButtonConfirmarClick() {
        Juego.calcularResultado(spinnerCanicas.value)
    }
}
