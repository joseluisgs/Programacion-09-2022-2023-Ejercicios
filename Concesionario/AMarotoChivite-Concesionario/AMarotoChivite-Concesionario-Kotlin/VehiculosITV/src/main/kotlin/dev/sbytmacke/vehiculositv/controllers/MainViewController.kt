package dev.sbytmacke.vehiculositv.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.sbytmacke.vehiculositv.enums.TypeOperation
import dev.sbytmacke.vehiculositv.mappers.toTypeMotor
import dev.sbytmacke.vehiculositv.models.Vehicule
import dev.sbytmacke.vehiculositv.routes.RoutesManager
import dev.sbytmacke.vehiculositv.states.DataState
import dev.sbytmacke.vehiculositv.viewmodels.MainViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Cursor.DEFAULT
import javafx.scene.Cursor.WAIT
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.format.DateTimeParseException

private val logger = KotlinLogging.logger {}

class MainViewController : KoinComponent {

    private val viewModel: MainViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuExportar: MenuItem

    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    // Botones
    @FXML
    private lateinit var buttonNuevo: Button

    @FXML
    private lateinit var buttonEditar: Button

    @FXML
    private lateinit var buttonDelete: Button

    //Combo
    @FXML
    private lateinit var comboTypeMotor: ComboBox<String>

    // Tabla
    @FXML
    private lateinit var tableVehicules: TableView<Vehicule>

    @FXML
    private lateinit var tableColumnId: TableColumn<Vehicule, Long>

    @FXML
    private lateinit var tableColumMatricule: TableColumn<Vehicule, String>

    @FXML
    private lateinit var tableColumnTypeMotor: TableColumn<Vehicule, Double>

    // Buscador
    @FXML
    private lateinit var textBuscador: TextField

    // Formulario del vehiculo
    @FXML
    private lateinit var textVehiculeId: TextField

    @FXML
    private lateinit var textVehiculeMatricule: TextField

    @FXML
    private lateinit var textVehiculeMarca: TextField

    @FXML
    private lateinit var textVehiculeModelo: TextField

    @FXML
    private lateinit var textVehiculeKm: TextField

    @FXML
    private lateinit var textTypeMotor: TextField

    // DatePicker
    @FXML
    private lateinit var datePickerVehiculeFechaMantenimiento: DatePicker

    @FXML
    fun initialize() {
        logger.debug { "Inicializando..." }

        // Iniciamos las conexiones de interfaz con los estados y la reactividad
        initBindings()
        initEventos()
    }

    private fun initBindings() {
        logger.debug { "Inicializando bindings" }

        // comboBoxes
        comboTypeMotor.items = FXCollections.observableArrayList(viewModel.stateVehicule.value.typesVehicules)
        comboTypeMotor.selectionModel.selectFirst()

        // Tablas
        tableVehicules.items = FXCollections.observableArrayList(viewModel.stateVehicule.value.vehicules)
        tableVehicules.selectionModel.selectionMode = SelectionMode.SINGLE

        // columnas, con el nombre de la propiedad del objeto hará binding
        tableColumnId.cellValueFactory = PropertyValueFactory("id")
        tableColumMatricule.cellValueFactory = PropertyValueFactory("matricule")
        tableColumnTypeMotor.cellValueFactory = PropertyValueFactory("typeMotor")

        // En el momento cambiemos el estado el viewmodel, reacciona
        viewModel.stateVehicule.addListener { _, oldState, newState ->

            // Formulario, es solo de lectura, solo si ha cambiado, que es costoso
            updateVehiculeDetailsOnlyLecture(oldState, newState)

            // Solo si ha cambiado la lista, limpiamos la selección, porque es una operación costosa
            updatesTabla(newState, oldState)
        }
    }

    private fun initEventos() {
        logger.debug { "Inicializando eventos" }

        // menús
        menuExportar.setOnAction {
            onExportarAction()
        }

        menuSalir.setOnAction {
            onOnCloseAction()
        }

        menuAcercaDe.setOnAction {
            onAcercaDeAction()
        }

        // Botones
        buttonNuevo.setOnAction {
            onNuevoAction()
        }

        buttonEditar.setOnAction {
            onEditarAction()
        }

        buttonDelete.setOnAction {
            onEliminarAction()
        }

        // Combo
        comboTypeMotor.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        // Tabla
        tableVehicules.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTablaSelected(it) }
        }

        // Buscador
        // Evento del buscador key press
        // Funciona con el intro
        // textBuscador.setOnAction {
        // con cualquer letra al levantarse, ya ha cambiado el valor
        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }
    }

    private fun updatesTabla(
        newState: DataState,
        oldState: DataState,
    ) {
        if (newState.vehicules != oldState.vehicules) {
            tableVehicules.selectionModel.clearSelection()
            tableVehicules.items = FXCollections.observableArrayList(viewModel.stateVehicule.value.vehicules)
        }
    }

    private fun updateVehiculeDetailsOnlyLecture(
        oldState: DataState,
        newState: DataState,
    ) {
        if (oldState.vehiculeSeleccionado != newState.vehiculeSeleccionado) {
            textVehiculeId.text =
                if (newState.vehiculeSeleccionado.id == Vehicule.NEW_VEHICULE) "" else newState.vehiculeSeleccionado.id.toString()

            textVehiculeMatricule.text = newState.vehiculeSeleccionado.matricule
            textVehiculeMarca.text = newState.vehiculeSeleccionado.marca
            textVehiculeModelo.text = newState.vehiculeSeleccionado.modelo
            textTypeMotor.text = newState.vehiculeSeleccionado.typeMotor.toString()
            textVehiculeKm.text = newState.vehiculeSeleccionado.km.toString()
            try {
                datePickerVehiculeFechaMantenimiento.value =
                    LocalDate.parse(newState.vehiculeSeleccionado.fechaMantenimiento)
            } catch (e: DateTimeParseException) {
                logger.debug { "Excepción $e" }
                // Pero me funciona correctamente..., aunque llegue aquí y salte la excepción del parseador, ES MUY RARO
            }
        }
    }

    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }

        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }

        // Filtramos por el tipo seleccionado en la tabla
        tableVehicules.items = FXCollections.observableList(
            viewModel.vehiculoFilteredList(toTypeMotor(comboTypeMotor.value), textBuscador.text.trim(), null)
        )
    }

    private fun onTablaSelected(newValue: Vehicule) {
        logger.debug { "onTablaSelected: $newValue" }

        viewModel.updateVehiculeSelected(newValue)
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }

        filterDataTable()
    }

    private fun onEliminarAction() {
        logger.debug { "onEliminarAction" }

        // Comprobar que se ha seleccionado antes!!
        if (tableVehicules.selectionModel.selectedItem == null) {
            return
        }

        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Eliminar Vehiculo"
            headerText = "¿Desea eliminar este vehiculo?"
            contentText = "Esta acción no se puede deshacer y se eliminarán todos los datos asociados al vehiculo."
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                if (viewModel.eliminarAlumno()) {
                    logger.debug { "Vehiculo eliminado correctamente" }
                    showAlertOperacion(
                        alerta = Alert.AlertType.INFORMATION,
                        title = "Vehiculo eliminado",
                        header = "Vehiculo eliminado del sistema",
                        mensaje = "Se ha eliminado el vehiculo correctamente del sistema de gestión."
                    )
                } else {
                    logger.error { "Error al eliminar el vehiculo" }
                    showAlertOperacion(alerta = Alert.AlertType.ERROR, "Error al eliminar el Vehiculo")
                }
            }
        }
    }

    private fun onEditarAction() {
        logger.debug { "onEditarAction" }

        // Si no tenemos un item seleccionado, no continuamos editando
        if (tableVehicules.selectionModel.selectedItem == null) {
            return
        }

        // Debido a la inmutabilidad del objeto lo copiamos y asignamos en el constructor que es un estado de edición
        viewModel.stateVehicule.value = viewModel.stateVehicule.value.copy(tipoOperacion = TypeOperation.EDITAR)

        RoutesManager.initDetailsVehiculeStage()
    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }

        // Debido a la inmutabilidad del objeto lo copiamos y asignamos en el constructor que es un estado nuevo
        viewModel.stateVehicule.value = viewModel.stateVehicule.value.copy(tipoOperacion = TypeOperation.NUEVO)

        RoutesManager.initDetailsVehiculeStage()
    }

    private fun onAcercaDeAction() {
        logger.debug { "onAcercaDeAction" }
        RoutesManager.initAcercaDeStage()
    }

    private fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = "Salir de Expedientes DAM"
            contentText = "¿Desea salir de Expedientes DAM?"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                Platform.exit()
            }
        }
    }

    private fun showAlertOperacion(
        alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION,
        title: String = "",
        header: String = "",
        mensaje: String = "",
    ) {
        Alert(alerta).apply {
            this.title = title
            this.headerText = header
            this.contentText = mensaje
        }.showAndWait()
    }

    private fun onExportarAction() {
        logger.debug { "onExportarAction" }

        val fileChooser = FileChooser()
        fileChooser.title = "Exportando vehiculos"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        val file = fileChooser.showSaveDialog(RoutesManager.getActiveStage())

        logger.debug { "onSaveAction" }

        RoutesManager.getActiveStage().scene.cursor = WAIT

        if (file != null) {
            viewModel.exportToJson(file).onSuccess {
                showAlertOperacion(
                    title = "Datos exportados",
                    header = "Datos exportados correctamente a fichero JSON",
                    mensaje = "Se han exportado los vehículos"
                )
            }.onFailure { e ->
                showAlertOperacion(alerta = Alert.AlertType.ERROR, title = "Error al exportar", mensaje = e.message)
            }
            RoutesManager.getActiveStage().scene.cursor = DEFAULT
        }
    }
}