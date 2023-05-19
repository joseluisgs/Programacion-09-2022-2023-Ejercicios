package com.jczhang.jczhangcocheskotlinjavafx.controller

import com.jczhang.jczhangcocheskotlinjavafx.models.Coche
import com.jczhang.jczhangcocheskotlinjavafx.routes.RoutesManager
import com.jczhang.jczhangcocheskotlinjavafx.viewmodel.CochesViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger { }

class CochesViewController : KoinComponent {


    private val viewModel: CochesViewModel by inject()

    //Campos de la derecha
    @FXML
    lateinit var matriculationMostrar: DatePicker

    @FXML
    lateinit var kmMostrar: TextField

    @FXML
    lateinit var modeloMostrar: TextField

    @FXML
    lateinit var tipoMotorMostrar: TextField

    @FXML
    lateinit var marcaMostrar: TextField

    @FXML
    lateinit var idMostrar: TextField

    //Tabla
    @FXML
    lateinit var tablaCoches: TableView<Coche>

    @FXML
    lateinit var tipoMotorTabla: TableColumn<Coche, String>

    @FXML
    lateinit var modeloTabla: TableColumn<Coche, String>

    @FXML
    lateinit var idTabla: TableColumn<Coche, String>

    // Menú de arriba
    @FXML
    lateinit var tipoFiltrado: ComboBox<String>

    @FXML
    lateinit var cocheBuscado: TextField


    @FXML
    fun initialize() {
        logger.info { "Iniciando controlador " }
        val style = "-fx-opacity: 1"

        idMostrar.style = style
        marcaMostrar.style = style
        modeloMostrar.style = style
        tipoMotorMostrar.style = style
        matriculationMostrar.style = style
        matriculationMostrar.editor.style = style
        kmMostrar.style = style

        initBindings()
        initEvents()
    }

    private fun initEvents() {
        //ComboBox
        tipoFiltrado.items = FXCollections.observableList(viewModel.listaTipos)
        tipoFiltrado.selectionModel.selectLast()

        //Menu de búsqueda de arriba
        tablaCoches.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTableSelected(it) }
        }
        tipoFiltrado.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        cocheBuscado.setOnKeyReleased { newValue
            ->
            newValue?.let { onTextAction() }
        }

    }

    private fun initBindings() {
        logger.debug { "Iniciando bindings" }
        //parte de la tabla
        tablaCoches.items = viewModel.state.coches
        idTabla.cellValueFactory = PropertyValueFactory("id")
        modeloTabla.cellValueFactory = PropertyValueFactory("modelo")
        tipoMotorTabla.cellValueFactory = PropertyValueFactory("tipoMotor")

        idMostrar.textProperty().bind(viewModel.state.cocheSeleccionado.id)
        marcaMostrar.textProperty().bind(viewModel.state.cocheSeleccionado.marca)
        modeloMostrar.textProperty().bind(viewModel.state.cocheSeleccionado.modelo)
        tipoMotorMostrar.textProperty().bind(viewModel.state.cocheSeleccionado.tipoMotor)
        kmMostrar.textProperty().bind(viewModel.state.cocheSeleccionado.km)
        matriculationMostrar.valueProperty().bind(viewModel.state.cocheSeleccionado.matriculacion)
    }


    private fun onTextAction() {
        logger.debug { "onTextAction" }
        filterDataTable()
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected $newValue" }
        filterDataTable()
    }


    private fun filterDataTable() {
        logger.debug { "FilterDataTable" }
        tablaCoches.items = viewModel.filteredCochesList(tipoFiltrado.value, cocheBuscado.text.trim())
    }

    private fun onTableSelected(newValue: Coche) {
        logger.debug { "onTableSelected" }
        viewModel.updateState(newValue)
    }


    fun onAddAction() {
        RoutesManager.initAddItemStage()
    }

    fun onEditarAction() {
        val cocheSeleccionado = tablaCoches.selectionModel.selectedItem
        if (cocheSeleccionado != null) {
            RoutesManager.initEditItemStage()
        }
    }

    fun onBorrarAction() {
        val cocheSeleccionado = tablaCoches.selectionModel.selectedItem
        if (cocheSeleccionado != null) {
            val idCoche = cocheSeleccionado.id
            viewModel.deleteFromDataBase(idCoche)
            viewModel.loadFromDatabase()

        }


    }

    fun onAcercaDeAction() {
        RoutesManager.initAcercaDeStage()
    }

    fun onCloseAction(event: Event) {
        logger.debug { "onCloseAction" }

        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.apply {
            headerText = "¿Está seguro de que desea salir?"
            contentText = "Se perderán todos los datos no guardados."

        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                Platform.exit()
            } else {
                event.consume()
            }
        }
    }
}
