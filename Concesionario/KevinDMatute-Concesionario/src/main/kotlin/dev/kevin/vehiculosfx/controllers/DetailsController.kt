package dev.kevin.vehiculosfx.controllers

import com.github.michaelbull.result.*
import dev.kevin.vehiculosfx.errors.VehiculoError
import dev.kevin.vehiculosfx.models.Vehiculo
import dev.kevin.vehiculosfx.routes.RoutesManager
import dev.kevin.vehiculosfx.viewmodels.VehiculoViewModel
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


private val logger = KotlinLogging.logger {  }

class DetailsController : KoinComponent {
    val viewModel: VehiculoViewModel by inject()

    @FXML
    private lateinit var textIdDetalle: TextField
    @FXML
    private lateinit var textIdMatricula: TextField
    @FXML
    private lateinit var textIdModelo: TextField
    @FXML
    private lateinit var textIdMarca:TextField
    @FXML
    private lateinit var comboMotor: ComboBox<String>
    @FXML
    private lateinit var textIdKm: TextField
    @FXML
    private lateinit var pickerFecha: DatePicker
    @FXML
    private lateinit var botonDetalleGuardar: Button
    @FXML
    private lateinit var botonLimpiar: Button
    @FXML
    private lateinit var botonCancelar: Button
    @FXML
    private lateinit var imagenDetalle: ImageView

    private var imageFileVehiculo: File? = null

    @FXML
    fun initialize(){
        textIdDetalle.isEditable = false

        pickerFecha.isEditable = false
        initBindings()
        initEventos()
        when(viewModel.state.value.vehiculoOperacion) {
            VehiculoViewModel.TipoOperacion.EDITAR -> {
                pickerFecha.isEditable = false
                textIdModelo.isEditable = false
            }
            else -> {
                //fechaPicker.isEditable = false
                limpiarFormulario()
            }
        }
    }

    private fun initEventos() {

        botonDetalleGuardar.setOnAction {
            onGuardarAction()
        }

        botonLimpiar.setOnAction {
            onLimpiarAction()
        }
        botonCancelar.setOnAction {
            onCancelarAction()
        }
        imagenDetalle.setOnMouseClicked {
            onImageAction()
        }
    }


    private fun onImageAction() {
        logger.debug { "onImageAction" }
        FileChooser().run {
            title = "Selecciona una imagen"
            extensionFilters.addAll(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            imagenDetalle.image = Image(it.toURI().toString())
            imageFileVehiculo = it
        }
    }

    private fun onCancelarAction() {
        cerrarVentana()
    }

    private fun cerrarVentana() {
        botonCancelar.scene.window.hide()
    }

    private fun onLimpiarAction() {
        limpiarFormulario()
    }

    private fun limpiarFormulario() {
        logger.debug { "limpiarFormulario" }
        if(viewModel.state.value.vehiculoOperacion == VehiculoViewModel.TipoOperacion.NUEVO){
            textIdDetalle.text = ""
        }
        textIdMatricula.text = ""
        textIdDetalle.text = ""
        textIdModelo.text = ""
        textIdMarca.text = ""
        comboMotor.value = null
        textIdKm.text = ""
        //fechaPicker = DatePicker()
        pickerFecha.value = null
        imagenDetalle.image = viewModel.getDefaultImage()
    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarAction" }
        when(viewModel.state.value.vehiculoOperacion){
            VehiculoViewModel.TipoOperacion.NUEVO -> {
                validarFormulario().andThen {
                    viewModel.crearVehiculo((it.copy(id = Vehiculo.NEW_VEHICULO)))
                }.onSuccess { a ->
                    logger.debug { "Vehiculo creado" }
                    showAlertOperacion(
                        Alert.AlertType.INFORMATION,
                        title = "Vehiculo Creado",
                        header = "El Vehiculo se ha almacenado correctamente",
                        mensaje = ""
                    )
                    cerrarVentana()
                }.onFailure {e ->
                    logger.error { "Error al guardar el Vehiculo" }
                    showAlertOperacion(
                        Alert.AlertType.ERROR,
                        title = "Error al guardar",
                        header = "Se produjo un error al guardar el vehiculo",
                        mensaje = "Error al guardarlo en el sistema de gestión"
                    )
                }
            }
            VehiculoViewModel.TipoOperacion.EDITAR -> {
                validarFormulario().andThen {
                    viewModel.editarVehiculo(it)
                }.onSuccess { a ->
                    logger.debug { "Vehiculo edtitado" }
                    showAlertOperacion(
                        Alert.AlertType.INFORMATION,
                        title = "Vehiculo Editado",
                        header = "El Vehiculo se ha actualizado correctamente",
                        mensaje = ""
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Vehiculo creado" }
                    showAlertOperacion(
                        Alert.AlertType.ERROR,
                        title = "Error al actualizar el vehiculo",
                        header = "Error produido al actualizar el Vehiculo",
                        mensaje = "Error al actualizarlo el sistema de gestión"
                    )
                    cerrarVentana()

                }
            }
        }
    }

    private fun validarFormulario(): Result<VehiculoViewModel.VehiculoState, VehiculoError> {
        logger.debug { "validarFormulario" }

        if(textIdMatricula.text.isNullOrEmpty() || !textIdMatricula.text.matches(Regex("(\\d{4})([BCDFGHJKLMNPRSTVWXYZ]{3})"))){
            logger.debug { "error en la matricula" }
            return Err(VehiculoError.ValidationError(" fecha no puede ser mayor a la actual"))
        }
        if(textIdMarca.text.isNullOrEmpty()){
            logger.debug { "error en la marca" }
            return Err(VehiculoError.ValidationError("La marca no puede estar vacia"))
        }
        if(textIdKm.text.isNullOrEmpty() || textIdKm.text.toInt() < 0){
            logger.debug { "error en los kilometros" }
            return Err(VehiculoError.ValidationError("Los kilometros no pueden estar vacions ni sesr menor de 0"))
        }
        if(pickerFecha.value == null){
            return Err(VehiculoError.ValidationError("sdfsdfs"))
        }
        if(pickerFecha.value.isAfter(LocalDate.now())){
            logger.debug { "error en la fecha" }
            return Err(VehiculoError.ValidationError("La fecha no puede ser mayor a la actual"))
        }
        return Ok(
            VehiculoViewModel.VehiculoState(
                id = if (textIdDetalle.text.isNullOrEmpty()) Vehiculo.NEW_VEHICULO else textIdDetalle.text.toLong(),
                matricula = textIdMatricula.text,
                marca = textIdMarca.text,
                modelo = textIdModelo.text,
                motor = comboMotor.value,
                km = textIdKm.text.toInt(),
                fechaMatriculacion = pickerFecha.value,
                imagen= imagenDetalle.image,
                fileImagen = imageFileVehiculo
            )
        )
    }

    private fun initBindings() {
        comboMotor.items = FXCollections.observableArrayList(viewModel.state.value.tiposMotor)
        comboMotor.selectionModel.selectFirst()

        if (viewModel.state.value.vehiculoOperacion == VehiculoViewModel.TipoOperacion.EDITAR){
            textIdDetalle.text =
                if(viewModel.state.value.vehiculoSeleccionado.id == Vehiculo.NEW_VEHICULO) "" else viewModel.state.value.vehiculoSeleccionado.id.toString()
            textIdMatricula.text = viewModel.state.value.vehiculoSeleccionado.matricula
            textIdModelo.text =  viewModel.state.value.vehiculoSeleccionado.modelo
            textIdMarca.text = viewModel.state.value.vehiculoSeleccionado.marca
            comboMotor.value = viewModel.state.value.vehiculoSeleccionado.motor
            textIdKm.text = viewModel.state.value.vehiculoSeleccionado.km.toString()
            pickerFecha.value = viewModel.state.value.vehiculoSeleccionado.fechaMatriculacion
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