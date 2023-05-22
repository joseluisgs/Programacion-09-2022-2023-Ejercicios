package dev.kevin.vehiculosfx.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.kevin.vehiculosfx.enums.tipoMotor
import dev.kevin.vehiculosfx.models.Vehiculo
import dev.kevin.vehiculosfx.routes.RoutesManager
import dev.kevin.vehiculosfx.viewmodels.VehiculoViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {  }

class HelloController : KoinComponent {

    val viewModel: VehiculoViewModel by inject()
    @FXML
    private lateinit var buscador: TextField
    @FXML
    private lateinit var comboTipo: ComboBox<String>
    @FXML
    private lateinit var tablaVehiculo: TableView<Vehiculo>
    @FXML
    private lateinit var tablaId: TableColumn<Vehiculo, Long>
    @FXML
    private lateinit var tablaNombre: TableColumn<Vehiculo, String>
    @FXML
    private lateinit var tablaMotor: TableColumn<Vehiculo, tipoMotor>
    @FXML
    private lateinit var botonNuevoVehiculo: Button
    @FXML
    private lateinit var botonEditar: Button
    @FXML
    private lateinit var botonEliminar: Button
    @FXML
    private lateinit var textoId: TextField
    @FXML
    private lateinit var textoModelo: TextField
    @FXML
    private lateinit var textoMarca: TextField
    @FXML
    private lateinit var textoMotor: TextField
    @FXML
    private lateinit var textoKm: TextField
    @FXML
    private lateinit var textofecha: TextField
    @FXML
    private lateinit var textMatricula: TextField
    @FXML
    private lateinit var imageVehiculo: ImageView

    @FXML
    fun initialize(){
        logger.debug { "Iniciando el controlador FXML" }
        textoId.isEditable = false
        textMatricula.isEditable = false
        textoKm.isEditable = false
        textofecha.isEditable = false
        textoMarca.isEditable = false
        textoMotor.isEditable = false
        textoModelo.isEditable = false

        initBindings()
        initEventos()
    }

    private fun initEventos() {
        logger.debug { "Iniciando eventos" }

        botonNuevoVehiculo.setOnAction {
            onNuevoAction()
        }

        botonEditar.setOnAction {
            onEditarAction()
        }

        botonEliminar.setOnAction {
            onEliminarAction()
        }

        comboTipo.selectionModel.selectedItemProperty().addListener{ _, _, newValue ->
            newValue?.let{ onComboSelected(it) }
        }

        buscador.setOnKeyReleased { newValue ->
            newValue?.let { onBuscadorRelease() }
        }

        tablaVehiculo.selectionModel.selectedItemProperty().addListener{ _, _, newValue ->
            newValue?.let { onTablaSelected(it) }
        }

    }

    private fun filterDataTabla() {
        logger.debug { "filterDataTable" }
        tablaVehiculo.items = FXCollections.observableList(
            viewModel.vehiculosFilteredList(comboTipo.value, buscador.text.trim())
        )
    }

    private fun onTablaSelected(it: Vehiculo) {
        logger.debug { "onTablaSelected: $it" }
        viewModel.updateVehiculoSeleccionado(it)
    }

    private fun onBuscadorRelease() {
        logger.debug { "onBuscadorRelease "}
        filterDataTabla()
    }

    private fun onComboSelected(it: String) {
        logger.debug { "onComboSelectedd: $it" }
        filterDataTabla()
    }

    private fun onEliminarAction() {
        logger.debug { "onEliminarAction" }
        if(tablaVehiculo.selectionModel.selectedItem == null){
            Alert(AlertType.INFORMATION).apply {
                title = "Sin Vehiculo"
                headerText = "No hay Vehiculos Seleccionados"
                contentText = "No se ha seleccionado ningun vehiculo para eliminar, seleccione alguno si quiere eliminarlo"
            }.showAndWait()
            return
        }
        Alert(AlertType.CONFIRMATION).apply {
            title = "Eliminar Vehiculo"
            headerText = "Estas seguro??"
            contentText = "Esto no se puede desace, se eliminara todo lo del vehiculo"
        }.showAndWait().ifPresent{ buttonType ->
            if(buttonType == ButtonType.OK){
                viewModel.eliminarVehiculo().onSuccess {
                    showAlertOperacion(
                        alerta = AlertType.INFORMATION,
                        title = "Vehiculo eliminado",
                        header = "Vehiculo eliminado del sistema",
                        mensaje = "Se ha eliminado una vehiculo correctamente"
                    )
                }.onFailure {
                    showAlertOperacion(
                        alerta = AlertType.ERROR, "Error al eliminar el vehiculo"
                    )
                }
            }
        }
    }

    private fun onEditarAction() {
        logger.debug { "onEditarAction" }
        if(tablaVehiculo.selectionModel.selectedItem == null){
            return
        }
        viewModel.setTipoOperacion(VehiculoViewModel.TipoOperacion.EDITAR)
        RoutesManager.initDetalle()
    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }
        viewModel.setTipoOperacion(VehiculoViewModel.TipoOperacion.NUEVO)
        RoutesManager.initDetalle()
    }

    private fun initBindings() {
        logger.debug { "initBindings" }
        comboTipo.items = FXCollections.observableArrayList(viewModel.state.value.tiposMotor)
        comboTipo.selectionModel.selectFirst()

        tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        tablaVehiculo.selectionModel.selectionMode = SelectionMode.SINGLE

        tablaId.cellValueFactory = PropertyValueFactory("marca")
        tablaMotor.cellValueFactory = PropertyValueFactory("motor")
        tablaNombre.cellValueFactory = PropertyValueFactory("modelo")

        viewModel.state.addListener{ _, oldState, newState->
            updatesFormulario(oldState, newState)
            updatesTabla(oldState, newState)
        }
        val styleOpacity = "fx-opacity: 1"
        textofecha.style = styleOpacity
    }

    private fun updatesTabla(oldState: VehiculoViewModel.ExpedienteState, newState: VehiculoViewModel.ExpedienteState){
        if(newState.vehiculos != oldState.vehiculos){
            tablaVehiculo.selectionModel.clearSelection()
            tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        }
    }

    private fun updatesFormulario(oldState: VehiculoViewModel.ExpedienteState, newState: VehiculoViewModel.ExpedienteState){
        if(oldState.vehiculoSeleccionado != newState.vehiculoSeleccionado){
            textoId.text =
                if (newState.vehiculoSeleccionado.id == Vehiculo.NEW_VEHICULO) "" else newState.vehiculoSeleccionado.id.toString()
            textMatricula.text = newState.vehiculoSeleccionado.marca
            textoMarca.text = newState.vehiculoSeleccionado.matricula
            textoModelo.text = newState.vehiculoSeleccionado.modelo
            textoMotor.text = newState.vehiculoSeleccionado.motor
            textoKm.text = newState.vehiculoSeleccionado.km.toString()
            textofecha.text = newState.vehiculoSeleccionado.fechaMatriculacion.toString()
            imageVehiculo.image = newState.vehiculoSeleccionado.imagen
        }
    }

    private fun showAlertOperacion(
        alerta: AlertType = AlertType.CONFIRMATION,
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

}