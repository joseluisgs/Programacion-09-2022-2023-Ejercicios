package dev.sergiosf.concesionario.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.routes.RoutesManager
import dev.sergiosf.concesionario.viewmodels.VehiculosViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {}

class ConcesionarioController : KoinComponent {

    val viewModel: VehiculosViewModel by inject()

    @FXML
    lateinit var textVehiculoMatricula: TextField

    @FXML
    lateinit var btnEliminar: Button

    @FXML
    lateinit var btnEditar: Button

    @FXML
    lateinit var btnNuevo: Button

    @FXML
    lateinit var textVehiculosMarca: TextField

    @FXML
    lateinit var dateVehiculosMatri: DatePicker

    @FXML
    lateinit var textVehiculosTipo: TextField

    @FXML
    lateinit var textVehiculosModelo: TextField

    @FXML
    lateinit var textVehiculosId: TextField

    @FXML
    lateinit var imageAlumno: ImageView

    @FXML
    lateinit var tableColumnMatricula: TableColumn<Vehiculo, String>

    @FXML
    lateinit var tableColumMarca: TableColumn<Vehiculo, String>

    @FXML
    lateinit var tableColumnId: TableColumn<Vehiculo, Long>

    @FXML
    lateinit var tableVehiculos: TableView<Vehiculo>

    @FXML
    lateinit var comboTipo: ComboBox<String>

    @FXML
    lateinit var menuAcercaDe: MenuItem

    @FXML
    lateinit var menuSalir: MenuItem

    lateinit var textBuscador: TextField

    @FXML
    fun initialize() {
        logger.debug { "Inicializando ExpedientesDeViewController FXML" }

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initBindings() {
        logger.debug { "Inicializando bindings" }

        // comboBoxe
        comboTipo.items = FXCollections.observableArrayList(viewModel.state.value.typeVehiculo.drop(1))
        comboTipo.selectionModel.selectFirst()

        // Tablas
        tableVehiculos.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        tableVehiculos.selectionModel.selectionMode = SelectionMode.SINGLE

        // columnas, con el nombre de la propiedad del objeto hará binding
        tableColumnId.cellValueFactory = PropertyValueFactory("id")
        tableColumMarca.cellValueFactory = PropertyValueFactory("marca")
        tableColumnMatricula.cellValueFactory = PropertyValueFactory("matricula")


        viewModel.state.addListener { _, oldState, newState ->
            updatesFormulario(oldState, newState)

            updatesTabla(newState, oldState)
        }

        val styleOpacity = "-fx-opacity: 1"
        dateVehiculosMatri.style = styleOpacity
        dateVehiculosMatri.editor.style = styleOpacity
    }

    private fun updatesFormulario(
        oldState: VehiculosViewModel.VehiculoState,
        newState: VehiculosViewModel.VehiculoState
    ) {
        if (oldState.vehiculoSeleccionado != newState.vehiculoSeleccionado) {
            textVehiculosId.text =
                if (newState.vehiculoSeleccionado.id == Vehiculo.NEW_VEHICULO) "" else newState.vehiculoSeleccionado.id.toString()
            textVehiculosMarca.text = newState.vehiculoSeleccionado.marca
            textVehiculosModelo.text = newState.vehiculoSeleccionado.modelo
            textVehiculoMatricula.text = newState.vehiculoSeleccionado.matricula
            textVehiculosTipo.text = newState.vehiculoSeleccionado.tipoVehiculo.value
            dateVehiculosMatri.value = newState.vehiculoSeleccionado.fechaMatriculation
            imageAlumno.image = newState.vehiculoSeleccionado.imagen
        }
    }

    private fun updatesTabla(
        newState: VehiculosViewModel.VehiculoState,
        oldState: VehiculosViewModel.VehiculoState
    ) {
        if (newState.vehiculos != oldState.vehiculos) {
            tableVehiculos.selectionModel.clearSelection()
            tableVehiculos.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        }
    }

    private fun initEventos() {
        menuAcercaDe.setOnAction {
            onAcercaDeAction()
        }

        menuSalir.setOnAction {
            onSalirAction()
        }

        btnNuevo.setOnAction {
            onNuevoAction()
        }

        btnEditar.setOnAction {
            onEditarAction()
        }

        btnEliminar.setOnAction {
            onEliminarAction()
        }

        // Combo
        comboTipo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        // Tabla
        tableVehiculos.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTablaSelected(it) }
        }

        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }
    }

    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }
        // filtramos por el tipo seleccionado en la tabla
        tableVehiculos.items = FXCollections.observableList(
            viewModel.vechiculosFilteredList(comboTipo.value, textBuscador.text.trim())
        )
    }

    private fun onTablaSelected(newValue: Vehiculo) {
        logger.debug { "onTablaSelected: $newValue" }
        viewModel.updateVehiculoSeleccionado(newValue)
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }
        filterDataTable()
    }

    private fun onEliminarAction() {
        logger.debug { "onEliminarAction" }

        if (tableVehiculos.selectionModel.selectedItem == null) {
            return
        }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Eliminar Vehiculo"
            headerText = "¿Desea eliminar este vehiculo?"
            contentText = "Esta acción no se puede deshacer y se eliminarán todos los datos asociados al vehiculo."
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                viewModel.eliminarVehiculo().onSuccess {
                    logger.debug { "Vehiculo eliminado correctamente" }
                    showAlertOperacion(
                            alerta = Alert.AlertType.INFORMATION,
                            title = "Vehiculo eliminado",
                            header = "Vehiculo eliminado del sistema",
                            mensaje = "Se ha eliminado el vehiculo correctamente del sistema de gestión."
                    )
                }.onFailure {
                    logger.error { "Error al eliminar el vehiculo: ${it.message}" }
                    showAlertOperacion(alerta = Alert.AlertType.ERROR, "Error al eliminar el vehiculo", it.message)
                }
            }
        }
    }

    private fun showAlertOperacion(
            alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION,
            title: String = "",
            header: String = "",
            mensaje: String = ""
    ) {
        Alert(alerta).apply {
            this.title = title
            this.headerText = header
            this.contentText = mensaje
        }.showAndWait()
    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }
        // Poner el modo nuevo antes!!
        viewModel.setTipoOperacion(VehiculosViewModel.TipoOperacion.NUEVO)
        RoutesManager.initDetalle()
    }

    private fun onEditarAction() {
        logger.debug { "onEditarAction" }
        if (tableVehiculos.selectionModel.selectedItem == null) {
            return
        }
        viewModel.setTipoOperacion(VehiculosViewModel.TipoOperacion.EDITAR)
        RoutesManager.initDetalle()
    }

    private fun onAcercaDeAction() {
        logger.debug { "onAcercaDeAction" }
        RoutesManager.initAboutStage()
    }

    private fun onSalirAction() {
        logger.debug { "onSalirAction" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = "Salir del concesionario"
            contentText = "¿Desea salir del concesionario"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                Platform.exit()
            }
        }
    }
}