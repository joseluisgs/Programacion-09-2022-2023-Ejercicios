package com.example.ejerciciobolaschinasivanrc.controllers

import com.example.ejerciciobolaschinasivanrc.error.AciertoError
import com.example.ejerciciobolaschinasivanrc.routes.RoutesManager
import com.example.ejerciciobolaschinasivanrc.viewmodel.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.VBox
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {  }

class MainController: KoinComponent {
    val viewModel: ViewModel by inject()

    @FXML
    private lateinit var cerrar: MenuItem

    @FXML
    private lateinit var acercaDe: MenuItem

    @FXML
    private lateinit var victOrdenador: Label

    @FXML
    private lateinit var victJugador: Label

    @FXML
    private lateinit var botonJugar: Button

    @FXML
    private lateinit var botonComenzar: Button

    @FXML
    private lateinit var spinnerRondas: Spinner<Int>

    @FXML
    private lateinit var spinnerBolas: Spinner<Int>

    @FXML
    private lateinit var spinnerBolasJug: Spinner<Int>

    @FXML
    private lateinit var spinnerAdivinar: Spinner<Int>

    @FXML
    private lateinit var cajaDeJuego: VBox

    @FXML
    private lateinit var cajaComienzo: VBox

    @FXML
    private lateinit var cajaTexto: TextArea

    @FXML
    private fun initialize(){
        cajaDeJuego.isVisible = false

        cajaTexto.textProperty().bind(viewModel.state.textView)
        victJugador.textProperty().bind(viewModel.state.victJugador.asString())
        victOrdenador.textProperty().bind(viewModel.state.victOrdenador.asString())

        spinnerBolas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(3, 7, 3, 2)
        spinnerRondas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1,3,1)

        botonComenzar.setOnAction {
            onBotonComenzarPartidaClick()
        }

        botonJugar.setOnAction {
            onBotonJugarClick()
        }

        cerrar.setOnAction {
            onCloseActionClick(it)
        }
        acercaDe.setOnAction {
            RoutesManager.initModelStage()
        }
    }

    private fun onBotonJugarClick() {
        logger.debug { "Se empieza la acción de jugar" }
        viewModel.state.bolasJugador = spinnerBolasJug.value
        viewModel.state.opcionJugador = spinnerAdivinar.value
        viewModel.state.bolasOrdenador = (0..viewModel.state.maxBolas).random()
        viewModel.state.opcionOrdenador = (0..viewModel.state.maxBolas * 2).random()

        viewModel.intentarAdivinar()
            .onFailure {
                viewModel.state.addAccion(it.message)
                noGanar(it)
            }
            .onSuccess {
                viewModel.state.addAccion(it.message)
                viewModel.state.rondasJugador++
                viewModel.state.addAccion("Tras la victoria por parte del jugador, se reaunuda con una nueva ronda.")
                viewModel.state.addAccion("Ronda actual: ${++viewModel.state.rondas}")
                viewModel.state.addAccion("Ronda ganadas por el ordenador: ${viewModel.state.rondasOrdenador}")
                viewModel.state.addAccion("Ronda ganadas por el jugador: ${viewModel.state.rondasJugador}")
                viewModel.state.addAccion("\n")
            }
        terminarPartida()
    }

    private fun terminarPartida() {
        logger.debug { "Comprobamos si se va ha terminar la partida o no" }
        val rondasAGanar = viewModel.state.maxRondas / 2 + 1
        val jugador = viewModel.state.rondasJugador
        val ordenador = viewModel.state.rondasOrdenador
        if(rondasAGanar <= jugador){
            viewModel.state.victJugador.value++
            Alert(AlertType.INFORMATION).apply {
                title = "El jugador ganó la partida."
                headerText = "El jugador ha ganado ${jugador} rondas, lo que le convierte en el ganador del mejor de ${viewModel.state.maxRondas} rondas."
                contentText = "En total el jugador lleva ${viewModel.state.victJugador.value} partidas ganadas."
            }.showAndWait()
            viewModel.state.rondas = 0
            viewModel.state.rondasOrdenador = 0
            viewModel.state.rondasJugador = 0
            cajaComienzo.isVisible = true
            cajaDeJuego.isVisible = false
        }
        if(rondasAGanar <= ordenador){
            viewModel.state.victOrdenador.value++
            Alert(AlertType.INFORMATION).apply {
                title = "El ordenador ganó la partida."
                headerText = "El ordenador ha ganado ${ordenador} rondas, lo que le convierte en el ganador del mejor de ${viewModel.state.maxRondas} rondas."
                contentText = "En total el ordenador lleva ${viewModel.state.victOrdenador.value} partidas ganadas."
            }.showAndWait()
            viewModel.state.rondas = 0
            viewModel.state.rondasOrdenador = 0
            viewModel.state.rondasJugador = 0
            cajaComienzo.isVisible = true
            cajaDeJuego.isVisible = false
        }
    }

    private fun noGanar(error: AciertoError) {
        when(error){
            is AciertoError.HuboEmpate -> {
                viewModel.state.addAccion("Tras este empate, se reaunuda con una nueva ronda.")
                viewModel.state.addAccion("Ronda actual: ${++viewModel.state.rondas}")
                viewModel.state.addAccion("Ronda ganadas por el ordenador: ${viewModel.state.rondasOrdenador}")
                viewModel.state.addAccion("Ronda ganadas por el jugador: ${viewModel.state.rondasJugador}")
                viewModel.state.addAccion("\n")
            }
            is AciertoError.MismaOpcion -> {
                viewModel.state.addAccion("Tras esta confusión, se reaunuda desde la misma ronda.")
                viewModel.state.addAccion("Ronda actual: ${viewModel.state.rondas}")
                viewModel.state.addAccion("Ronda ganadas por el ordenador: ${viewModel.state.rondasOrdenador}")
                viewModel.state.addAccion("Ronda ganadas por el jugador: ${viewModel.state.rondasJugador}")
                viewModel.state.addAccion("\n")
            }
            else -> {
                viewModel.state.addAccion("Tras la victoria por parte del ordenador, se reaunuda con una nueva ronda.")
                viewModel.state.addAccion("Ronda actual: ${++viewModel.state.rondas}")
                viewModel.state.addAccion("Ronda ganadas por el ordenador: ${++viewModel.state.rondasOrdenador}")
                viewModel.state.addAccion("Ronda ganadas por el jugador: ${viewModel.state.rondasJugador}")
                viewModel.state.addAccion("\n")
            }
        }
    }

    private fun onBotonComenzarPartidaClick() {
        logger.debug { "Se pulsa el boton de comenzar partida" }
        cajaDeJuego.isVisible = true
        cajaComienzo.isVisible = false
        viewModel.state.maxBolas = spinnerBolas.value
        viewModel.state.maxRondas = spinnerRondas.value
        spinnerAdivinar.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, viewModel.state.maxBolas*2, 0)
        spinnerBolasJug.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, viewModel.state.maxBolas, 0)
        viewModel.state.textView.value = "Bienvenido al juego de las bolas chinas\nLa configuración seleccionada ha sido:\nMáximo de bolas posibles: ${viewModel.state.maxBolas}\nMáximo de rondas posibles: ${viewModel.state.maxRondas}"
    }

    fun onCloseActionClick(event: Event){
        logger.debug { "Se trata de salir de la app" }
        Alert(AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la app?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la aplicación de las bolas chinas."
        }.showAndWait().ifPresent {
            if(it == ButtonType.OK){
                Platform.exit()
            }else{
                event.consume()
            }
        }
    }
}