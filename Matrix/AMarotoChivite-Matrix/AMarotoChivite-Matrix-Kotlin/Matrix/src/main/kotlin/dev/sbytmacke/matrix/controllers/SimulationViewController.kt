package dev.sbytmacke.matrix.controllers

import dev.sbytmacke.matrix.routes.RouterManager
import dev.sbytmacke.matrix.viewmodels.SimulationViewModel
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.*
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

class SimulationViewController : KoinComponent {

    private val viewModel: SimulationViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    // Areas de texto
    @FXML
    private lateinit var textOperacion: TextArea

    @FXML
    private lateinit var textGenerateChar: TextField

    @FXML
    private lateinit var textCuadrante: TextArea

    @FXML
    private lateinit var textTiempo: TextField

    // Botones
    @FXML
    lateinit var botonComenzar: Button

    @FXML
    fun initialize() {
        logger.info { "Inicializando" }

        // Eventos de los menus
        menuSalir.setOnAction { onOnCloseAction() }
        menuAcercaDe.setOnAction { onAcercaDeAction() }

        // Eventos de los botones
        botonComenzar.setOnAction { onComenzarAction() }

        // Hacemos el binding de los datos con el state
        viewModel.stateSimulation.addListener { _, _, newValue ->
            // Aquí actualizamos la vista
            Platform.runLater {
                textOperacion.text = newValue.operacion
                textCuadrante.text = newValue.cuadrante
                textGenerateChar.text = newValue.generatedCharacters.toString()
                textTiempo.text = newValue.tiempoActual.toString()
            }
        }
    }

    private fun onComenzarAction() {
        logger.info { "Comenzando simulación" }

        botonComenzar.isDisable = true

        println("Comenzando simulación")
        botonComenzar.scene.cursor = Cursor.WAIT

        // Ejecutamos en otro hilo para no bloquear la interfaz, si se realiza otra acción
        thread { viewModel.startSimulation() }
    }

    private fun onAcercaDeAction() {
        RouterManager.initAcercaDeStage()
    }

    private fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        Alert(Alert.AlertType.CONFIRMATION).apply {
            headerText = "Salir de App"
            title = "Salir de App"
            contentText = "¿Desea salir?"
        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                Platform.exit()
            }
        }
    }
}