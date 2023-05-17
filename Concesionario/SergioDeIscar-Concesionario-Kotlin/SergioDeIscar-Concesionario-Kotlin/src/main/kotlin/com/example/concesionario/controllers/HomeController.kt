package com.example.concesionario.controllers

import com.example.concesionario.models.Vehicle
import com.example.concesionario.states.ActionType
import com.example.concesionario.viewmodel.VehicleViewModel
import com.example.javafxdemo.routes.RoutesManager
import com.example.javafxdemo.routes.Views
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HomeController: KoinComponent {
    private val viewModel: VehicleViewModel by inject()

    @FXML
    private lateinit var menuAcercaDe: MenuItem
    @FXML
    private lateinit var menuExit: MenuItem
    @FXML
    private lateinit var menuImport: MenuItem
    @FXML
    private lateinit var menuExport: MenuItem

    @FXML
    private lateinit var tableVehiculo: TableView<Vehicle>
    @FXML
    private lateinit var columnId: TableColumn<Vehicle, Long>
    @FXML
    private lateinit var columnMarca: TableColumn<Vehicle, String>
    @FXML
    private lateinit var columnModelo: TableColumn<Vehicle, String>

    @FXML
    private lateinit var textId: TextField
    @FXML
    private lateinit var textMarca: TextField
    @FXML
    private lateinit var textModelo: TextField
    @FXML
    private lateinit var textMotor: TextField
    @FXML
    private lateinit var textMatriculacion: TextField
    @FXML
    private lateinit var textFilter: TextField
    @FXML
    private lateinit var textNumCoches: TextField

    @FXML
    private lateinit var imageVehicle: ImageView

    @FXML
    private lateinit var buttonNuevo: Button
    @FXML
    private lateinit var buttonEdit: Button
    @FXML
    private lateinit var buttonBorrar: Button

    @FXML
    private lateinit var panelDetalle: VBox
    @FXML
    private lateinit var splitMainPanel: SplitPane

    @FXML
    private lateinit var choiseFilter: ChoiceBox<String>

    @FXML
    private fun initialize() {
        // Paneles
        splitMainPanel.setDividerPositions(1.0)
        panelDetalle.isVisible = false

        // Binding
        initBinding();

        // Eventos
        initEvents();
    }

    private fun initBinding() {
        // ComboBox
        choiseFilter.items = FXCollections.observableArrayList(viewModel.state.value.motores)
        choiseFilter.selectionModel.selectFirst()

        // TableView
        tableVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.vehicles)
        tableVehiculo.selectionModel.selectionMode = SelectionMode.SINGLE

        // TableColumns
        columnId.cellValueFactory = PropertyValueFactory("id")
        columnMarca.cellValueFactory = PropertyValueFactory("marca")
        columnModelo.cellValueFactory = PropertyValueFactory("modelo")

        // TextFields
        textNumCoches.text = viewModel.state.value.vehicles.count().toString()

        viewModel.state.addListener { _, _, newState ->
            textId.text = newState.id
            textMarca.text = newState.marca
            textModelo.text = newState.modelo
            textMotor.text = newState.motor
            textMatriculacion.text = newState.fechaMatriculacion
            textNumCoches.text = newState.vehicles.count().toString()
            //imageVehicle.image = Image(RoutesManager.getResourceAsStream(newState.imagenUrl))
            try {
                imageVehicle.image = Image(newState.imagenUrl)
                imageVehicle.isCache = true

            } catch (e: Exception) {
                imageVehicle.image = Image(RoutesManager.getResourceAsStream(newState.imagenUrl))
            }
            tableVehiculo.items = FXCollections.observableArrayList(newState.vehicles)
        }
    }

    private fun initEvents() {
        // Menu
        menuAcercaDe.setOnAction {
            RoutesManager.changeScene(
                Views.ACERCA_DE,
                "Acerca de"
            )
        }

        menuExit.setOnAction {
            RoutesManager.exitAlert()
        }

        menuImport.setOnAction {
            choseTypeOfImport()
        }

        menuExport.setOnAction {
            choseTypeOfExport()
        }

        // ComboBox
        choiseFilter.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { updateMotorSelected() }
        }

        // TextField
        textFilter.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }

        // Table
        tableVehiculo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTableSelected(it) }
            if (newValue == null) {
                panelDetalle.isVisible = false
                splitMainPanel.setDividerPositions(1.0)
            } else {
                panelDetalle.isVisible = true
                splitMainPanel.setDividerPositions(0.55)
            }
        }

        // Buttons
        buttonNuevo.setOnAction {
            RoutesManager.openModal(
                Views.FORM,
                "Nuevo vehículo",
            )
            viewModel.state.value = viewModel.state.value.copy(actionType = ActionType.NEW)
        }

        buttonEdit.setOnAction {
            RoutesManager.openModal(
                Views.FORM,
                "Editar vehículo"
            )
            viewModel.state.value = viewModel.state.value.copy(actionType = ActionType.EDIT)
        }

        buttonBorrar.setOnAction {
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Borrar vehículo"
                headerText = "¿Está seguro de que desea borrar el vehículo?"
                contentText = "Esta acción no se puede deshacer"
            }.showAndWait().ifPresent {
                if (it == ButtonType.OK) {
                    viewModel.deleteVehicle()
                }
            }
        }
    }

    private fun choseTypeOfImport(){
        val choices: List<String> = listOf("JSON", "CSV (EN FUTURAS VERSIONES)")

        ChoiceDialog("JSON", choices).apply {
            title = "Importar"
            headerText = "Seleccione el tipo de importación"
            contentText = "Tipo de importación:"
        }.showAndWait().ifPresent {
            when(it){
                "JSON" -> viewModel.importVehiclesFromJson()
                "CSV" -> viewModel.importVehiclesFromCsv()
            }
        }
    }

    private fun choseTypeOfExport(){
        val choices: List<String> = listOf("JSON", "CSV (EN FUTURAS VERSIONES)")

        ChoiceDialog("JSON", choices).apply {
            title = "Exportar"
            headerText = "Seleccione el tipo de exportación"
            contentText = "Tipo de exportación:"
        }.showAndWait().ifPresent {
            when(it){
                "JSON" -> viewModel.exportVehiclesToJson()
                "CSV" -> viewModel.exportVehiclesToCsv()
            }
        }
    }

    private fun onKeyReleasedAction() {
        filterDataTable()
    }

    private fun updateMotorSelected() {
        filterDataTable()
    }

    private fun filterDataTable() {
        tableVehiculo.items = FXCollections.observableArrayList(
            viewModel.vehiclesFilteredList(choiseFilter.value, textFilter.text.trim())
        )
    }

    private fun onTableSelected(it: Vehicle) {
        viewModel.updateVehicleSelected(it)
    }
}