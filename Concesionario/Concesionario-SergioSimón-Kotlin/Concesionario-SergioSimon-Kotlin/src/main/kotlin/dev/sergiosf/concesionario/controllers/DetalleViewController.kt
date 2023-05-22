package dev.sergiosf.concesionario.controllers

import com.github.michaelbull.result.*
import dev.sergiosf.concesionario.errors.VehiculoError
import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.routes.RoutesManager
import dev.sergiosf.concesionario.viewmodels.VehiculosViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class DetalleViewController : KoinComponent {

    private val viewModel: VehiculosViewModel by inject()

    @FXML
    lateinit var btnCancelar: Button

    @FXML
    lateinit var btnLimpiar: Button

    @FXML
    lateinit var btnGuardar: Button

    @FXML
    lateinit var comboTipoVehiculo: ComboBox<String>

    @FXML
    lateinit var textVehiculoMarca: TextField

    @FXML
    lateinit var dateVehiculoFech: DatePicker

    @FXML
    lateinit var textVehiculoModelo: TextField

    @FXML
    lateinit var textVehiculoMatricula: TextField

    @FXML
    lateinit var textVehiculoId: TextField

    @FXML
    lateinit var imageVehiculo: ImageView

    private var imageFileVehiculo: File? = null


    @FXML
    fun initialize() {
        logger.debug { "Inicializando DetalleViewController FXML en Modo: ${viewModel.state.value.tipoOperacion}" }

        textVehiculoId.isEditable = false // No se puede editar el número

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initEventos() {
        // Botones
        btnGuardar.setOnAction {
            onGuardarAction()
        }

        btnLimpiar.setOnAction {
            onLimpiarAction()
        }

        btnCancelar.setOnAction {
            onCancelarAction()
        }

        imageVehiculo.setOnMouseClicked {
            onImageAction()
        }

        comboTipoVehiculo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }

    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarActio" }

        // Dependiendo del modo
        when (viewModel.state.value.tipoOperacion) {

            VehiculosViewModel.TipoOperacion.NUEVO -> {
                validateForm().andThen {
                    viewModel.crearVehiculo(it.copy(id = Vehiculo.NEW_VEHICULO))
                }.onSuccess { success ->
                    logger.debug { "Alumno creado correctamente" }
                    showAlertOperacion(
                            Alert.AlertType.INFORMATION,
                            title = "Vehículo creado",
                            header = "Vehículo creado almacenado correctamente",
                            mensaje = "Se ha salvado en el sistema de gestión:\n${success.modelo}"
                    )
                    cerrarVentana()
                }.onFailure { error ->
                    logger.error { "Error al salvar vehículo/a: ${error.message}" }
                    showAlertOperacion(
                            Alert.AlertType.ERROR,
                            title = "Error al salvar vehículo",
                            header = "Se ha producido un error al salvar el vehículo",
                            mensaje = "Se ha producido un error al salvar en el sistema de gestión a:\n${error.message}"
                    )
                }
            }

            VehiculosViewModel.TipoOperacion.EDITAR -> {
                validateForm().andThen {
                    viewModel.editarVehiculo(it)
                }.onSuccess { error ->
                    logger.debug { "Vehículo editado correctamente" }
                    showAlertOperacion(
                            Alert.AlertType.INFORMATION,
                            "Vehículo editado",
                            header = "Vehículo editado correctamente",
                            mensaje = "Se ha salvado en el sistema de gestión:\n${error.modelo}"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al actualizar Vehículo: ${e.message}" }
                    showAlertOperacion(
                            Alert.AlertType.ERROR,
                            title = "Error al actualizar Vehículo",
                            header = "Se ha producido un error al actualizar el Vehículo",
                            mensaje = "Se ha producido un error al actualizar el sistema de gestión:\n${e.message}"
                    )
                }
            }
        }
    }

    private fun onLimpiarAction() {
        logger.debug { "onLimpiarAction" }
        limpiarForm()
    }

    private fun limpiarForm() {
        if (viewModel.state.value.tipoOperacion == VehiculosViewModel.TipoOperacion.NUEVO) {
            textVehiculoId.text = ""
        }
        textVehiculoModelo.text = ""
        textVehiculoMarca.text = ""
        textVehiculoMatricula.text = ""
        dateVehiculoFech.value = null
        imageVehiculo.image = viewModel.getDefautltImage()
    }

    private fun initBindings() {

        comboTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.typeVehiculo.drop(1))
        comboTipoVehiculo.selectionModel.selectFirst()

        if (viewModel.state.value.tipoOperacion == VehiculosViewModel.TipoOperacion.EDITAR) {
            textVehiculoId.text =
                    if (viewModel.state.value.vehiculoSeleccionado.id == Vehiculo.NEW_VEHICULO) "" else viewModel.state.value.vehiculoSeleccionado.id.toString()
            textVehiculoMarca.text = viewModel.state.value.vehiculoSeleccionado.marca
            textVehiculoModelo.text = viewModel.state.value.vehiculoSeleccionado.modelo
            textVehiculoMatricula.text = viewModel.state.value.vehiculoSeleccionado.matricula
            comboTipoVehiculo.value = viewModel.state.value.vehiculoSeleccionado.tipoVehiculo.value
            dateVehiculoFech.value = viewModel.state.value.vehiculoSeleccionado.fechaMatriculation
            imageVehiculo.image = viewModel.state.value.vehiculoSeleccionado.imagen
        }
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        cerrarVentana()
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }

    private fun onImageAction() {
        logger.debug { "onImageAction" }
        // Abrimos un diálogo para seleccionar una imagen, esta vez lo he hecho más compacto!!!
        // Comparalo con los de Json!!!
        FileChooser().run {
            title = "Selecciona una imagen"
            extensionFilters.addAll(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            imageVehiculo.image = Image(it.toURI().toString())
            imageFileVehiculo = it
        }
    }

    private fun validateForm(): Result<VehiculosViewModel.VehiculoFormulario, VehiculoError> {
        logger.debug { "validateForm" }

        // Validacion del formulario
        if (textVehiculoMarca.text.isNullOrEmpty()) {
            return Err(VehiculoError.ValidationProblem("La marca no puede estar vacío"))
        }
        if (textVehiculoModelo.text.isNullOrEmpty()) {
            return Err(VehiculoError.ValidationProblem("El modelo no puede estar vacío"))
        }
        if (textVehiculoMatricula.text.isNullOrEmpty()) {
            return Err(VehiculoError.ValidationProblem("La matricula no puede estar vacío"))
        }
        if (dateVehiculoFech.value == null || dateVehiculoFech.value.isAfter(LocalDate.now())) {
            return Err(VehiculoError.ValidationProblem("Fecha de nacimiento no puede estar vacía y debe ser anterior a hoy"))
        }
        return Ok(
                VehiculosViewModel.VehiculoFormulario(
                        id = if (textVehiculoId.text.isNullOrEmpty()) Vehiculo.NEW_VEHICULO else textVehiculoId.text.toLong(),
                        marca = textVehiculoMarca.text,
                        modelo = textVehiculoModelo.text,
                        matricula = textVehiculoMatricula.text,
                        tipoVehiculo = comboConverter(comboTipoVehiculo.value),
                        fechaMatriculation = dateVehiculoFech.value,
                        imagen = imageVehiculo.image,
                        fileImage = imageFileVehiculo
                )
        )
    }

    private fun comboConverter(value: String?): Vehiculo.TipoVehiculo {
        return when (value) {
            Vehiculo.TipoVehiculo.GASOLINA.value -> Vehiculo.TipoVehiculo.GASOLINA
            Vehiculo.TipoVehiculo.ELECTRICO.value -> Vehiculo.TipoVehiculo.ELECTRICO
            Vehiculo.TipoVehiculo.DIESEL.value -> Vehiculo.TipoVehiculo.DIESEL
            Vehiculo.TipoVehiculo.HIBRIDO.value -> Vehiculo.TipoVehiculo.HIBRIDO
            else -> Vehiculo.TipoVehiculo.NONE
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
}
