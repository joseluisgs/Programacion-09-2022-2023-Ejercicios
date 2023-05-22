package com.example.gestionvehiculosconimagenes_kotlin.controllers

import com.example.gestionvehiculosconimagenes_kotlin.mapper.getTipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.routes.RoutesManager
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.SharedState
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.TipoOperacion
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {  }

class VehiculosController: KoinComponent {

    //named("MySQL")
    val viewModel: ViewModel by inject()

    @FXML
    private lateinit var menuCloseBoton: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    @FXML
    private lateinit var textFieldMarcaModelo: TextField

    @FXML
    private lateinit var comboBox: ComboBox<String>

    @FXML
    private lateinit var tablaVehiculo: TableView<Vehiculo>

    @FXML
    private lateinit var columnaIdVehiculo: TableColumn<Vehiculo, Long>

    @FXML
    private lateinit var columnaMarcaModelo: TableColumn<Vehiculo, String>

    @FXML
    private lateinit var columnaTipoMotor: TableColumn<Vehiculo, String>

    @FXML
    private lateinit var idVehiculo: TextField

    @FXML
    private lateinit var imagenVehiculo: ImageView

    @FXML
    private lateinit var matricula: TextField

    @FXML
    private lateinit var marca: TextField

    @FXML
    private lateinit var modelo: TextField

    @FXML
    private lateinit var tipoMotor: TextField

    @FXML
    private lateinit var kilometros: TextField

    @FXML
    private lateinit var fechaMatriculacion: DatePicker

    @FXML
    private lateinit var botonAñadir: Button

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonBorrar: Button

    @FXML
    fun initialize(){
        initBinds()

        initEvents()

        initStyle()
    }

    private fun initStyle() {
        logger.debug { "Iniciamos estilos ha aplicar a nuestro programa" }

        val styleOpacity = "-fx-opacity: 1"
        fechaMatriculacion.style = styleOpacity
        fechaMatriculacion.editor.style = styleOpacity
        idVehiculo.style = styleOpacity
        matricula.style = styleOpacity
        marca.style = styleOpacity
        modelo.style = styleOpacity
        tipoMotor.style = styleOpacity
        kilometros.style = styleOpacity
    }

    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        eventosDeLaTabla()

        menuCloseBoton.setOnAction {
            onCloseActionClick(it)
        }

        menuAcercaDe.setOnAction {
            onActionOpenModalView()
        }

        botonAñadir.setOnAction {
            onClickAniadirVehiculo()
        }

        botonEditar.setOnAction {
            onClickEditarIntroducirVehiculo()
        }

        botonBorrar.setOnAction {
            onClickEliminarIntroducirVehiculo()
        }
    }

    private fun onActionOpenModalView() {
        logger.debug { "Se a pulsado el botón de abrir la modal view" }
        RoutesManager.initModalView()
    }

    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la aplicación" }

        comboBox.items = FXCollections.observableArrayList(viewModel.state.value.comboBoxOptions)
        comboBox.selectionModel.selectFirst()

        tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        tablaVehiculo.selectionModel.selectionMode = SelectionMode.SINGLE

        columnaIdVehiculo.cellValueFactory = PropertyValueFactory("id")
        columnaMarcaModelo.cellValueFactory = PropertyValueFactory("marcaModelo")
        columnaTipoMotor.cellValueFactory = PropertyValueFactory("tipoMotorText")

        viewModel.state.addListener { _, oldstate, newstate ->
            updateVehiculoDataCases(oldstate, newstate)
            updateTabla(oldstate, newstate)
        }
    }

    private fun updateTabla(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if(oldstate.vehiculos != newstate.vehiculos){
            logger.debug { "Se actualizán los datos de la tabla" }
            tablaVehiculo.selectionModel.clearSelection()
            tablaVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos)
        }
    }

    private fun updateVehiculoDataCases(
        oldstate: SharedState,
        newstate: SharedState
    ) {
        if (oldstate.vehiculoReference != newstate.vehiculoReference) {
            logger.debug { "Actualizamos los datos del vehículo que se muestra por pantalla" }
            idVehiculo.text =
                if(newstate.vehiculoReference.id == Vehiculo.VEHICULO_ID) "" else newstate.vehiculoReference.id.toString()
            matricula.text = newstate.vehiculoReference.matricula
            marca.text = newstate.vehiculoReference.marca
            modelo.text = newstate.vehiculoReference.modelo
            tipoMotor.text =
                if(newstate.vehiculoReference.tipoMotor == null) "" else newstate.vehiculoReference.tipoMotor.toString()
            kilometros.text =
                if(viewModel.state.value.vehiculoReference.km == 0.0) "" else newstate.vehiculoReference.km.toString()
            fechaMatriculacion.value = newstate.vehiculoReference.fechaMatriculacion
            imagenVehiculo.image = newstate.vehiculoReference.foto
        }
    }

    private fun eventosDeLaTabla() {
        logger.debug { "Iniciamos los eventos que afectán directamente a la tabla" }

        tablaVehiculo.selectionModel.selectedItemProperty().addListener { _, _, vehiculo ->
            vehiculo?.let { viewModel.onSelectedUpdateVehiculoReference(vehiculo) }
        }

        textFieldMarcaModelo.setOnKeyReleased {
            onKeyReleaseFilterTableData()
        }

        comboBox.selectionModel.selectedItemProperty().addListener{_, _, tipoMotor ->
            tipoMotor?.let{onComboSelectFilterTableData()}
        }
    }

    private fun onClickEliminarIntroducirVehiculo() {
        logger.debug { "Se inicia la acción de intertar eliminar un vehículo" }
        if(tablaVehiculo.selectionModel.selectedItem == null){
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al ejecutar acción"
                headerText = "No has seleccionado ningún vehículo!!!"
                contentText = null
            }.showAndWait()
            return
        }
        val id = viewModel.state.value.vehiculoReference.id
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Estás a punto de eliminar un vehículo?"
            headerText = null
            contentText = "¿Seguro que deseas eliminar al vehículo de id: $id?"
        }.showAndWait().ifPresent{
            if(it == ButtonType.OK){
                viewModel.deleteVehiculo()
                    .onSuccess {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Se ha eliminado al vehículo de id: $id"
                        }.showAndWait()
                    }
                    .onFailure {
                        Alert(Alert.AlertType.INFORMATION).apply {
                            title = "Operación terminada"
                            headerText = null
                            contentText = "Ha habido un error inesperado: ${it.message}"
                        }.showAndWait()
                    }

            }else{
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Operación cancelada"
                    headerText = null
                    contentText = "Se cancela la operación de eliminar el vehículo de id:  $id"
                }.showAndWait()
            }
        }
    }

    private fun onClickEditarIntroducirVehiculo() {
        logger.debug { "Se habré la ventana modal, para la edición de el vehículo seleccionado" }
        if(tablaVehiculo.selectionModel.selectedItem == null){
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al ejecutar acción"
                headerText = "No has seleccionado ningún vehículo!!!"
                contentText = null
            }.showAndWait()
            return
        }
        viewModel.setTipoOperacion(TipoOperacion.EDITAR)
        RoutesManager.initDetalleViewVehiculo()
    }

    private fun onClickAniadirVehiculo() {
        logger.debug { "Se habré la ventana modal, para la creación de un nuevo vehículo" }
        viewModel.setTipoOperacion(TipoOperacion.AÑADIR)
        RoutesManager.initDetalleViewVehiculo()
    }

    private fun onComboSelectFilterTableData() {
        logger.debug { "Se filtran los datos de la tabla tras seleccionar un tipo de motor" }
        filterTableData()
    }

    private fun onKeyReleaseFilterTableData() {
        logger.debug { "Se filtán los vehículos de la tabla tras escribir en el filtro" }
        filterTableData()
    }

    private fun filterTableData() {
        logger.debug { "se filtran los datos de la tabla" }
        tablaVehiculo.items = FXCollections.observableArrayList(
            viewModel.filterTable(
                comboBox.value.getTipoMotor(),
                textFieldMarcaModelo.text
            )
        )
    }

    fun onCloseActionClick(event: Event){
        logger.debug { "Se trata de salir de la app" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la app?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la aplicación de gestión de los vehículos."
        }.showAndWait().ifPresent {
            if(it == ButtonType.OK){
                Platform.exit()
            }else{
                event.consume()
            }
        }
    }
}