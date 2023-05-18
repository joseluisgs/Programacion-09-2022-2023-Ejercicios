package dev.joseluisgs.expedientesacademicos.controllers

import com.github.michaelbull.result.*
import dev.joseluisgs.expedientesacademicos.errors.CocheError
import dev.joseluisgs.expedientesacademicos.locale.toLocalNumber
import dev.joseluisgs.expedientesacademicos.models.Coche
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import dev.joseluisgs.expedientesacademicos.validators.validate
import dev.joseluisgs.expedientesacademicos.viewmodels.ConcesionarioViewModel
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
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
    // Inyectamos nuestro ViewModel
    val viewModel: ConcesionarioViewModel by inject()

    // Botones
    @FXML
    private lateinit var btnGuardar: Button

    @FXML
    private lateinit var btnLimpiar: Button

    @FXML
    private lateinit var btnCancelar: Button

    // Formulario del alumno
    @FXML
    private lateinit var textCocheNumero: TextField

    @FXML
    private lateinit var textCocheMatricula: TextField

    @FXML
    private lateinit var textCocheMarca: TextField

    @FXML
    private lateinit var textCocheModelo: TextField

    @FXML
    private lateinit var textCocheMotor: TextField

    @FXML
    private lateinit var dateCocheFechaMatriculacion: DatePicker

    @FXML
    private lateinit var imageCoche: ImageView

    private var imageFileCoche: File? = null

    @FXML
    fun initialize() {
        textCocheNumero.isEditable = false // No se puede editar el número

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

        imageCoche.setOnMouseClicked {
            onImageAction()
        }
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
            imageCoche.image = Image(it.toURI().toString())
            imageFileCoche = it
        }

    }

    private fun initBindings() {
        // Si es el modo de edición, cargamos los datos del alumno
        if (viewModel.state.value.tipoOperacion == ConcesionarioViewModel.TipoOperacion.EDITAR) {
            textCocheNumero.text =
                if (viewModel.state.value.cocheSeleccionado.numero == Coche.NEW_COCHE) "" else viewModel.state.value.cocheSeleccionado.numero.toString()
            textCocheMatricula.text = viewModel.state.value.cocheSeleccionado.matricula
            textCocheMarca.text = viewModel.state.value.cocheSeleccionado.marca
            textCocheModelo.text = viewModel.state.value.cocheSeleccionado.modelo
            textCocheMotor.text = viewModel.state.value.cocheSeleccionado.tipoMotor
            dateCocheFechaMatriculacion.value = viewModel.state.value.cocheSeleccionado.fechaMatriculacion
            imageCoche.image = viewModel.state.value.cocheSeleccionado.imagen
        }
    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarActio" }

        // Dependiendo del modo
        when (viewModel.state.value.tipoOperacion) {

            ConcesionarioViewModel.TipoOperacion.NUEVO -> {
                validateForm().andThen {
                    viewModel.crearCoche(it.copy(numero = Coche.NEW_COCHE))
                }.onSuccess { a ->
                    logger.debug { "Coche creado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        title = "Coche creado",
                        header = "Coche creado y almacenado correctamente",
                        mensaje = "Se ha salvado en el sistema de gestión:\n${a.marca} ${a.modelo}"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al salvar coche: ${e.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al salvar coche",
                        header = "Se ha producido un error al salvar el coche",
                        mensaje = "Se ha producido un error al salvar en el sistema de gestión a:\n${e.message}"
                    )
                }
            }

            ConcesionarioViewModel.TipoOperacion.EDITAR -> {
                validateForm().andThen {
                    viewModel.editarCoche(it)
                }.onSuccess { a ->
                    logger.debug { "Coche editado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        "Coche editado",
                        header = "Coche editado correctamente",
                        mensaje = "Se ha salvado en el sistema de gestión:\n${a.marca} ${a.modelo}"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al actualizar coche: ${e.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al actualizar coche",
                        header = "Se ha producido un error al actualizar el coche",
                        mensaje = "Se ha producido un error al actualizar el sistema de gestión:\n${e.message}"
                    )
                }
            }
        }
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        cerrarVentana()
    }

    private fun onLimpiarAction() {
        logger.debug { "onLimpiarAction" }
        limpiarForm()
    }

    private fun limpiarForm() {
        // Limpiamos el estado actual
        if (viewModel.state.value.tipoOperacion == ConcesionarioViewModel.TipoOperacion.NUEVO) {
            // si es nuevo lo ponemos a "", pero si es editar no lo tocamos
            textCocheNumero.text = ""
        }
        textCocheMatricula.text = ""
        textCocheMarca.text = ""
        textCocheModelo.text = ""
        textCocheMotor.text = ""
        dateCocheFechaMatriculacion.value = null
        imageCoche.image = viewModel.getDefautltImage()
    }

    // Lo puedo hacer aquí o en mi validador en el viewModel
    private fun validateForm(): Result<ConcesionarioViewModel.CocheFormulario, CocheError> {
        logger.debug { "validateForm" }

        // Validacion del formulario
        if (!textCocheMatricula.text.matches("""^[0-9]{4}-[BCDFGHJKLMNPQRSTVWXYZ]{3}${'$'}""".toRegex())) {
            return Err(CocheError.ValidationProblem("La matrícula no cumple el formato correcto (sólo se admite NNNN-LLL)"))
        }
        if (textCocheMarca.text.isNullOrEmpty()) {
            return Err(CocheError.ValidationProblem("La marca no puede estar vacía"))
        }
        if (textCocheModelo.text.isNullOrEmpty()) {
            return Err(CocheError.ValidationProblem("El modelo no puede estar vacío"))
        }
        if (textCocheMotor.text !in Coche.TipoMotor.values().map { it.toString() }) {
            return Err(CocheError.ValidationProblem("El tipo de motor no es válido"))
        }
        if (dateCocheFechaMatriculacion.value == null || dateCocheFechaMatriculacion.value.isAfter(LocalDate.now())) {
            return Err(CocheError.ValidationProblem("La fecha de matriculación no puede estar vacía ni ser posterior a la actual"))
        }
        return Ok(
            ConcesionarioViewModel.CocheFormulario(
                numero = if (textCocheNumero.text.isNullOrEmpty()) Coche.NEW_COCHE else textCocheNumero.text.toLong(),
                matricula = textCocheMatricula.text,
                marca = textCocheMarca.text,
                modelo = textCocheModelo.text,
                tipoMotor = textCocheMotor.text,
                fechaMatriculacion = dateCocheFechaMatriculacion.value,
                imagen = imageCoche.image,
                fileImage = imageFileCoche,
            )
        )
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
