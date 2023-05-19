package es.sergiomisas.concesionario.controllers

import com.github.michaelbull.result.*
import es.sergiomisas.concesionario.errors.CocheError
import es.sergiomisas.concesionario.models.Coche
import es.sergiomisas.concesionario.routes.RoutesManager
import es.sergiomisas.concesionario.viewmodels.ConcesionarioViewModel
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

    // Formulario del coche
    @FXML
    private lateinit var textCocheNumero: TextField

    @FXML
    private lateinit var textCocheMatricula: TextField

    @FXML
    private lateinit var textCocheMarca: TextField

    @FXML
    private lateinit var textCocheModelo: TextField

    @FXML
    private lateinit var textCocheTipoMotor: TextField
    
    @FXML
    private lateinit var dateCocheFechaMatriculacion: DatePicker
    
    @FXML
    private lateinit var imagecoche: ImageView

    private var imageFilecoche: File? = null

    @FXML
    fun initialize() {
        // logger.debug { "Inicializando DetalleViewController FXML en Modo: ${viewModel.state.tipoOperacion}" }

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
        imagecoche.setOnMouseClicked {
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
            imagecoche.image = Image(it.toURI().toString())
            imageFilecoche = it
        }

    }

    private fun initBindings() {
        // Si es el modo de edición, cargamos los datos del coche
        if (viewModel.state.value.tipoOperacion == ConcesionarioViewModel.TipoOperacion.EDITAR) {

            textCocheNumero.text =
                if (viewModel.state.value.cocheSeleccionado.numero == Coche.NEW_COCHE) "" else viewModel.state.value.cocheSeleccionado.numero.toString()
            textCocheMatricula.text = viewModel.state.value.cocheSeleccionado.matricula
            textCocheMarca.text = viewModel.state.value.cocheSeleccionado.marca
            textCocheModelo.text = viewModel.state.value.cocheSeleccionado.modelo
            dateCocheFechaMatriculacion.value = viewModel.state.value.cocheSeleccionado.fechaMatriculacion
            textCocheTipoMotor.text = viewModel.state.value.cocheSeleccionado.tipoMotor
            imagecoche.image = viewModel.state.value.cocheSeleccionado.imagen
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
                    logger.debug { "coche creado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        title = "coche creado",
                        header = "coche creado almacenado correctamente",
                        mensaje = "Se ha salvado en el sistema de gestión:\n${a.marcaYModelo}"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al salvar coche/a: ${e.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al salvar coche/a",
                        header = "Se ha producido un error al salvar el coche/a",
                        mensaje = "Se ha producido un error al salvar en el sistema de gestión a:\n${e.message}"
                    )
                }
            }

            ConcesionarioViewModel.TipoOperacion.EDITAR -> {
                validateForm().andThen {
                    viewModel.editarCoche(it)
                }.onSuccess { a ->
                    logger.debug { "coche editado correctamente" }
                    showAlertOperacion(
                        AlertType.INFORMATION,
                        "coche editado",
                        header = "coche editado correctamente",
                        mensaje = "Se ha salvado en el sistema de gestión:\n${a.marcaYModelo}"
                    )
                    cerrarVentana()
                }.onFailure { e ->
                    logger.error { "Error al actualizar coche/a: ${e.message}" }
                    showAlertOperacion(
                        AlertType.ERROR,
                        title = "Error al actualizar coche/a",
                        header = "Se ha producido un error al actualizar el coche/a",
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
        dateCocheFechaMatriculacion.value = null
        textCocheTipoMotor.text = ""
        imagecoche.image = viewModel.getDefautltImage()
    }

    // Lo puedo hacer aquí o en mi validador en el viewModel
    private fun validateForm(): Result<ConcesionarioViewModel.CocheFormulario, CocheError> {
        logger.debug { "validateForm" }

        // Validacion del formulario
        if (!textCocheMatricula.text.matches(Regex("[0-9]{4}[A-Z]{3}"))) {
            return Err(CocheError.ValidationProblem("La matricula tiene que ser válida ej: 1234ABC"))
        }
        if (textCocheMarca.text.isNullOrEmpty()) {
            return Err(CocheError.ValidationProblem("Marca no puede estar vacía"))
        }
        if (textCocheModelo.text.isNullOrEmpty()) {
            return Err(CocheError.ValidationProblem("El modelo no puede estar vacío"))
        }
        if (dateCocheFechaMatriculacion.value == null || dateCocheFechaMatriculacion.value.isAfter(LocalDate.now())) {
            return Err(CocheError.ValidationProblem("Fecha de matriculacion no puede estar vacía y debe ser anterior a hoy"))
        }
        if (textCocheTipoMotor.text !in Coche.TipoMotor.values().map { it.name } || textCocheTipoMotor.text == "TODOS") {
            return Err(CocheError.ValidationProblem("El tipo de motor no es válido"))
        }
        return Ok(
            ConcesionarioViewModel.CocheFormulario(
                numero = if (textCocheNumero.text.isNullOrEmpty()) Coche.NEW_COCHE else textCocheNumero.text.toLong(),
                matricula = textCocheMatricula.text,
                marca = textCocheMarca.text,
                modelo = textCocheModelo.text,
                fechaMatriculacion = dateCocheFechaMatriculacion.value,
                tipoMotor = textCocheTipoMotor.text,
                imagen = imagecoche.image,
                fileImage = imageFilecoche
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
