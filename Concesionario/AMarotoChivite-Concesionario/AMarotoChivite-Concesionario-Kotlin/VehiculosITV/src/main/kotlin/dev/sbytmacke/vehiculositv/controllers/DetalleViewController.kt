package dev.sbytmacke.vehiculositv.controllers

import com.github.michaelbull.result.*
import dev.sbytmacke.vehiculositv.enums.TypeOperation
import dev.sbytmacke.vehiculositv.errors.VehiculeError
import dev.sbytmacke.vehiculositv.mappers.toTypeMotor
import dev.sbytmacke.vehiculositv.models.Vehicule
import dev.sbytmacke.vehiculositv.states.VehiculeDetailsState
import dev.sbytmacke.vehiculositv.viewmodels.MainViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class DetalleViewController : KoinComponent {

    private val viewModel: MainViewModel by inject()

    // Botones
    @FXML
    private lateinit var buttonSave: Button

    @FXML
    private lateinit var buttonClean: Button

    @FXML
    private lateinit var buttonCancel: Button

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
    private lateinit var comboTypeMotorDetails: ComboBox<String>

    @FXML
    private lateinit var datePickerVehiculeFechaMantenimiento: DatePicker

    @FXML
    fun initialize() {
        logger.debug { "Inicializando..." }

        textVehiculeId.isEditable = false // No se puede editar el id

        // Iniciamos las conexiones de interfaz con los estados y la reactividad
        initBindings()
        initEventos()
    }

    private fun initEventos() {
        // Botones
        buttonSave.setOnAction {
            onGuardarAction()
        }
        buttonClean.setOnAction {
            onLimpiarAction()
        }
        buttonCancel.setOnAction {
            onCancelarAction()
        }
    }

    private fun initBindings() {
        val vehicleSelected = viewModel.stateVehicule.value.vehiculeSeleccionado

        // comboBoxes
        comboTypeMotorDetails.items =
            FXCollections.observableArrayList(viewModel.stateVehicule.value.typesVehicules.filter { it != Vehicule.TypeMotor.ALL.value && it != Vehicule.TypeMotor.NONE.value })
        comboTypeMotorDetails.selectionModel.selectFirst()

        // Si es el modo de edición, cargamos los datos del vehiculo
        if (viewModel.stateVehicule.value.tipoOperacion == TypeOperation.EDITAR) {
            if (vehicleSelected.id == Vehicule.NEW_VEHICULE) {
                textVehiculeId.text = ""
            } else {
                textVehiculeId.text = vehicleSelected.id.toString()
            }
            textVehiculeMatricule.text = vehicleSelected.matricule
            textVehiculeMarca.text = vehicleSelected.marca
            textVehiculeModelo.text = vehicleSelected.modelo
            comboTypeMotorDetails.value = vehicleSelected.typeMotor.toString()
            textVehiculeKm.text = vehicleSelected.km.toString()
            datePickerVehiculeFechaMantenimiento.value = LocalDate.parse(vehicleSelected.fechaMantenimiento)
        }
    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarAction" }

        // Dependiendo del modo será NUEVO o EDITAR
        when (viewModel.stateVehicule.value.tipoOperacion) {
            TypeOperation.NUEVO -> {
                validateFormNotNull().andThen {
                    viewModel.createVehicule(it.copy(id = Vehicule.NEW_VEHICULE))
                }.onSuccess {
                    logger.debug { "Vehiculo creado correctamente" }

                    showAlertOperacion(
                        AlertType.INFORMATION,
                        title = "Vehiculo creado",
                        header = "Vehiculo creado almacenado correctamente",
                        mensaje = "Se ha salvado en el sistema"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al salvar el vehiculo: ${e.message}" }

                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al salvar el vehiculo",
                        header = "Se ha producido un error al salvar el vehiculo",
                        mensaje = "Se ha producido un error al salvar en el sistema: \n${e.message}"
                    )
                }
            }

            TypeOperation.EDITAR -> {
                validateFormNotNull().andThen {
                    viewModel.updateVehicule(it)
                }.onSuccess {
                    logger.debug { "Vehiculo editado correctamente" }

                    showAlertOperacion(
                        AlertType.INFORMATION,
                        title = "Vehiculo editado",
                        header = "Vehiculo editado y almacenado correctamente",
                        mensaje = "Se ha editado en el sistema"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al actualizar el vehiculo: ${e.message}" }

                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al actualizar el vehiculo",
                        header = "Se ha producido un error al actualizar el vehiculo",
                        mensaje = "Se ha producido un error al actualizar el sistema :\n${e.message}"
                    )
                }
            }
        }
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        buttonCancel.scene.window.hide()
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        cerrarVentana()
    }

    private fun onLimpiarAction() {
        logger.debug { "onLimpiarAction" }
        limpiarForm()
    }

    private fun validateFormNotNull(): Result<VehiculeDetailsState, VehiculeError> {
        logger.debug { "validateForm" }

        // Validacion del formulario
        if (textVehiculeMatricule.text.isNullOrEmpty()) {
            return Err(VehiculeError.EmptyField())
        }

        val matriculeDuplicate = viewModel.stateVehicule.value.vehicules.find { it!!.matricule == textVehiculeMatricule.text  }
        if (matriculeDuplicate != null) {
            return Err(VehiculeError.MatriculaInvalid("La matrícula ${textVehiculeMatricule.text} ya existe!"))
        }

        if (textVehiculeMatricule.text.isNullOrEmpty()) {
            return Err(VehiculeError.EmptyField())
        }
        if (textVehiculeMarca.text.isNullOrEmpty()) {
            return Err(VehiculeError.EmptyField())
        }
        if (textVehiculeModelo.text.isNullOrEmpty()) {
            return Err(VehiculeError.EmptyField())
        }
        if (textVehiculeKm.text.isNullOrEmpty()) {
            return Err(VehiculeError.EmptyField())
        }

        try {
            textVehiculeKm.text.toDouble()
        } catch (e: Exception) {
            return Err(VehiculeError.KmInvalid("El Km debe ser un número decimal. Ej: 150.0"))
        }

        try {
            LocalDate.parse(datePickerVehiculeFechaMantenimiento.value.toString())
        } catch (e: Exception) {
            return Err(VehiculeError.FechaMantenimientoInvalid("Introduce la fecha mediante el calendario!"))
        }

        return Ok(
            VehiculeDetailsState(
                Vehicule.NEW_VEHICULE,
                textVehiculeMatricule.text,
                textVehiculeMarca.text,
                textVehiculeModelo.text,
                toTypeMotor(comboTypeMotorDetails.value),
                textVehiculeKm.text.toDouble(),
                datePickerVehiculeFechaMantenimiento.value.toString(),
            )
        )
    }

    private fun limpiarForm() {
        if (viewModel.stateVehicule.value.tipoOperacion == TypeOperation.NUEVO) {
            // Si estamos en edición no tocamos el ID
            textVehiculeId.text = ""
        }
        textVehiculeMatricule.text = ""
        textVehiculeMarca.text = ""
        textVehiculeModelo.text = ""
        comboTypeMotorDetails.selectionModel.selectFirst()
        textVehiculeKm.text = 0.0.toString()
        datePickerVehiculeFechaMantenimiento.value = null
    }

    private fun showAlertOperacion(
        alerta: AlertType = AlertType.CONFIRMATION,
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


}



