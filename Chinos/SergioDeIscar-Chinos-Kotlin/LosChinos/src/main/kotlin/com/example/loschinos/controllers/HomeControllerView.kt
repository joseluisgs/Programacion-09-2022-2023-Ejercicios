package com.example.loschinos.controllers

import com.example.javafxdemo.routers.RoutesManager
import com.example.javafxdemo.routers.Views
import com.example.loschinos.errors.ChinosError
import com.example.loschinos.viewmodels.ChinosViewModel
import com.github.michaelbull.result.mapBoth
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import org.koin.java.KoinJavaComponent.inject
import kotlin.system.exitProcess

class HomeControllerView {
    //val viewModel: ChinosViewModel by inject() // No me funciona
    private val viewModel by inject<ChinosViewModel>(ChinosViewModel::class.java)

    @FXML
    private lateinit var panelConfig: VBox
    @FXML
    private lateinit var panelPartida: VBox

    @FXML
    private lateinit var imageLogo: ImageView

    @FXML
    private lateinit var spinnerRondas: Spinner<Int>
    @FXML
    private lateinit var spinnerBolas: Spinner<Int>
    @FXML
    private lateinit var spinnerApuesta: Spinner<Int>
    @FXML
    private lateinit var spinnerPlayerSelect: Spinner<Int>

    @FXML
    private lateinit var buttonAceptar: Button
    @FXML
    private lateinit var buttonDefault: Button
    @FXML
    private lateinit var buttonApostar: Button

    @FXML
    private lateinit var menuAcercaDe: MenuItem
    @FXML
    private lateinit var menuExit: MenuItem

    @FXML
    private lateinit var textVictoriasPlayer: TextField
    @FXML
    private lateinit var textVictoriasIa: TextField
    @FXML
    private lateinit var textRondas: TextField

    @FXML
    private fun initialize() {
        println("Home Controller View")

        // Paneles
        panelConfig.isVisible = true
        panelPartida.isVisible = false

        // Imagenes
        imageLogo.image = Image(RoutesManager.getResourceAsStream("images/icon.png"))

        // Spinners
        spinnerRondas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1, 2)
        spinnerBolas.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5)

        // Botones
        buttonDefault.setOnAction { setDefaultConfig() }

        buttonAceptar.setOnAction { startGame() }

        buttonApostar.setOnAction {
            // La primera vez que se llama desactiva el spinner de seleccion de jugador
            spinnerPlayerSelect.isDisable = true
            viewModel.apostar().mapBoth(
                success = { result ->
                    Alert(
                        Alert.AlertType.INFORMATION
                    ).apply {
                        title = "Apuesta"
                        headerText = "Ganaste!!!"
                        contentText = result
                    }.showAndWait()
                },
                failure = { error ->
                    when(error){
                        is ChinosError.ApuestaNadieGana -> {
                            Alert(
                                Alert.AlertType.INFORMATION
                            ).apply {
                                title = "Apuesta"
                                headerText = "Nadie gana!!!"
                                contentText = error.message
                            }.showAndWait()
                        }
                        is ChinosError.ApuestaPerdida -> {
                            Alert(
                                Alert.AlertType.INFORMATION
                            ).apply {
                                title = "Apuesta"
                                headerText = "Perdiste!!!"
                                contentText = error.message
                            }.showAndWait()
                        }
                        is ChinosError.ApuestaEmpate -> {
                            Alert(
                                Alert.AlertType.INFORMATION
                            ).apply {
                                title = "Apuesta"
                                headerText = "Empate ._."
                                contentText = error.message
                            }.showAndWait()
                        }
                    }
                }
            )
        }

        // Menus
        menuAcercaDe.setOnAction { RoutesManager.openModal(Views.ACERCA_DE, "Acerca De") }

        menuExit.setOnAction {
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Salir"
                headerText = "¿Estás seguro que quieres salir?"
                contentText = "Si sales perderás los datos de la partida"
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent { Platform.exit() }
        }

        // Bindings
        spinnerRondas.valueFactory.valueProperty().bindBidirectional(viewModel.state.rondas)
        spinnerBolas.valueFactory.valueProperty().bindBidirectional(viewModel.state.bolasMax)

        textVictoriasPlayer.textProperty().bind(viewModel.state.victoriasUser.asString())
        textVictoriasIa.textProperty().bind(viewModel.state.victoriasIA.asString())
        textRondas.textProperty().bind(viewModel.state.rondas.asString())

        viewModel.state.rondas.addListener { _, _, newValue ->
            if (newValue.toInt() == 0) {
                panelConfig.isVisible = true
                panelPartida.isVisible = false
                setDefaultConfig()
            }
            spinnerPlayerSelect.isDisable = false // Habilita el spinner de seleccion de jugador para la siguiente ronda
        }
    }

    private fun startGame(){
        // Paneles
        panelConfig.isVisible = false
        panelPartida.isVisible = true

        // Spinners
        spinnerPlayerSelect.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, viewModel.state.bolasMax.value, 0)
        spinnerApuesta.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, viewModel.state.bolasMax.value * 2, 0)

        // Bindings
        spinnerApuesta.valueFactory.valueProperty().bindBidirectional(viewModel.state.bolasUserApuesta)
        spinnerPlayerSelect.valueFactory.valueProperty().bindBidirectional(viewModel.state.bolasUserSelect)

        viewModel.iaApuesta()
    }

    private fun setDefaultConfig() {
        viewModel.state.rondas.value = 1
        viewModel.state.bolasMax.value = 5
    }
}