package dev.joseluisgs.expedientesacademicos.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.joseluisgs.expedientesacademicos.models.Coche
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import dev.joseluisgs.expedientesacademicos.viewmodels.ConcesionarioViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Cursor.DEFAULT
import javafx.scene.Cursor.WAIT
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private val logger = KotlinLogging.logger {}

class ConcesionarioViewController : KoinComponent {
    // Inyectamos nuestro ViewModel
    val viewModel: ConcesionarioViewModel by inject()

    // Menus
    @FXML
    private lateinit var menuImportar: MenuItem

    @FXML
    private lateinit var menuExportar: MenuItem

    @FXML
    private lateinit var menuZip: MenuItem

    @FXML
    private lateinit var menuUnzip: MenuItem

    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    // Botones
    @FXML
    private lateinit var btnNuevo: Button

    @FXML
    private lateinit var btnEditar: Button

    @FXML
    private lateinit var btnEliminar: Button

    //Combo
    @FXML
    private lateinit var comboTipo: ComboBox<String>

    // Tabla
    @FXML
    private lateinit var tableCoches: TableView<Coche>

    @FXML
    private lateinit var tableColumnNumero: TableColumn<Coche, Long>

    @FXML
    private lateinit var tableColumnModelo: TableColumn<Coche, String>

    @FXML
    private lateinit var tableColumnMatricula: TableColumn<Coche, Double>

    // Buscador
    @FXML
    private lateinit var textBuscador: TextField

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

    // Metodo para inicializar
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
        comboTipo.items = FXCollections.observableArrayList(viewModel.state.value.typesMotor)
        comboTipo.selectionModel.selectFirst()

        // Tablas
        tableCoches.items = FXCollections.observableArrayList(viewModel.state.value.coches)
        tableCoches.selectionModel.selectionMode = SelectionMode.SINGLE

        // columnas, con el nombre de la propiedad del objeto hará binding
        tableColumnNumero.cellValueFactory = PropertyValueFactory("id")
        tableColumnModelo.cellValueFactory = PropertyValueFactory("modelo")
        tableColumnMatricula.cellValueFactory = PropertyValueFactory("matricula")

        viewModel.state.addListener { _, oldState, newState ->
            // Formulario, es solo de lectura, solo si ha cambiado, que es costoso
            updatesFormulario(oldState, newState)

            // Solo si ha cambiado la lista de coches, limpiamos la selección
            // poque es una operación costosa
            updatesTabla(newState, oldState)
        }

        // Para que no se vea desactivado mucho, que queda feo!!
        val styleOpacity = "-fx-opacity: 1"
        dateCocheFechaMatriculacion.style = styleOpacity
        dateCocheFechaMatriculacion.editor.style = styleOpacity
    }

    private fun updatesTabla(
        newState: ConcesionarioViewModel.ConcesionarioState,
        oldState: ConcesionarioViewModel.ConcesionarioState
    ) {
        if (newState.coches != oldState.coches) {
            tableCoches.selectionModel.clearSelection()
            tableCoches.items = FXCollections.observableArrayList(viewModel.state.value.coches)
        }
    }

    private fun updatesFormulario(
        oldState: ConcesionarioViewModel.ConcesionarioState,
        newState: ConcesionarioViewModel.ConcesionarioState
    ) {
        if (oldState.cocheSeleccionado != newState.cocheSeleccionado) {
            textCocheNumero.text =
                if (newState.cocheSeleccionado.numero == Coche.NEW_COCHE) "" else newState.cocheSeleccionado.numero.toString()
            textCocheMatricula.text = newState.cocheSeleccionado.matricula
            textCocheMarca.text = newState.cocheSeleccionado.marca
            textCocheModelo.text = newState.cocheSeleccionado.modelo
            textCocheMotor.text = newState.cocheSeleccionado.tipoMotor
            dateCocheFechaMatriculacion.value = newState.cocheSeleccionado.fechaMatriculacion
            imageCoche.image = newState.cocheSeleccionado.imagen
        }
    }

    private fun initEventos() {
        logger.debug { "Inicializando eventos" }

        // menús
        menuImportar.setOnAction {
            onImportarAction()
        }

        menuExportar.setOnAction {
            onExportarAction()
        }

        menuZip.setOnAction {
            onZipAction()
        }

        menuUnzip.setOnAction {
            onUnzipAction()
        }

        menuSalir.setOnAction {
            onOnCloseAction()
        }

        menuAcercaDe.setOnAction {
            onAcercaDeAction()
        }

        // Botones
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
        tableCoches.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
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

    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }
        // filtramos por el tipo seleccionado en la tabla
        tableCoches.items = FXCollections.observableList(
            viewModel.cochesFilteredList(comboTipo.value, textBuscador.text)
        )
    }

    private fun onTablaSelected(newValue: Coche) {
        logger.debug { "onTablaSelected: $newValue" }
        viewModel.updateCocheSeleccionado(newValue)
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }
        filterDataTable()
    }

    private fun onEliminarAction() {
        logger.debug { "onEliminarAction" }
        // Comprbar que se ha seleccionado antes!!
        if (tableCoches.selectionModel.selectedItem == null) {
            return
        }
        Alert(AlertType.CONFIRMATION).apply {
            title = "Eliminar Coche"
            headerText = "¿Desea eliminar este coche?"
            contentText = "Esta acción no se puede deshacer y se eliminarán todos los datos asociados al coche."
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                viewModel.eliminarCoche().onSuccess {
                    logger.debug { "Coche eliminado correctamente" }
                    showAlertOperacion(
                        alerta = AlertType.INFORMATION,
                        title = "Coche eliminado",
                        header = "Coche eliminado del sistema",
                        mensaje = "Se ha eliminado el coche correctamente del sistema de gestión."
                    )
                }.onFailure {
                    logger.error { "Error al eliminar el coche: ${it.message}" }
                    showAlertOperacion(alerta = AlertType.ERROR, "Error al eliminar el coche", it.message)
                }
            }
        }
    }

    private fun onEditarAction() {
        logger.debug { "onEditarAction" }
        if (tableCoches.selectionModel.selectedItem == null) {
            return
        }
        viewModel.setTipoOperacion(ConcesionarioViewModel.TipoOperacion.EDITAR)
        RoutesManager.initDetalle()
    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }
        // Poner el modo nuevo antes!!
        viewModel.setTipoOperacion(ConcesionarioViewModel.TipoOperacion.NUEVO)
        RoutesManager.initDetalle()
    }

    private fun onAcercaDeAction() {
        logger.debug { "onAcercaDeAction" }
        RoutesManager.initAcercaDeStage()
    }

    private fun onExportarAction() {
        logger.debug { "onExportarAction" }
        // Forma larga, muy Java
        //val fileChooser = FileChooser()
        //fileChooser.title = "Exportar coches"
        //fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        //val file = fileChooser.showSaveDialog(RoutesManager.activeStage)

        // Forma Kotlin con run y let (scope functions)
        FileChooser().run {
            title = "Exportar coches"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showSaveDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onSaveAction: $it" }
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.saveAlumnadoToJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        header = "Datos exportados correctamente a fichero JSON",
                        mensaje = "Se ha exportado tus Coches desde el fichero de gestión.\nCoches exportados: ${viewModel.state.value.coches.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

    private fun onImportarAction() {
        logger.debug { "onImportarAction" }
        // Forma larga, muy Java
        //val fileChooser = FileChooser()
        //fileChooser.title = "Importar coches"
        //fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        //val file = fileChooser.showOpenDialog(RoutesManager.activeStage)

        // Forma Kotlin con run y let (scope functions)
        FileChooser().run {
            title = "Importar coches"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION,
                title = "Importando datos",
                header = "Importando datos desde JSON",
                mensaje = "Importando datos Se sustituye la imagen por una imagen por defecto."
            )
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.loadAlumnadoFromJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados",
                        header = "Datos importados correctamente desde JSON",
                        mensaje = "Se ha importado tus Coches al sistema de gestión.\nCoches importados: ${viewModel.state.value.coches.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

    // Método para salir de la aplicación
    fun onOnCloseAction() {
        logger.debug { "onOnCloseAction" }

        Alert(AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = "Salir de Concesionario DAM"
            contentText = "¿Desea salir de Concesionario DAM?"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                Platform.exit()
            }
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

    private fun onUnzipAction() {
        logger.debug { "onUnzipAction" }
        FileChooser().run {
            title = "Importar desde Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION, title = "Importando datos",
                header = "Importando datos desde un fichero ZIP",
                mensaje = "Se ha importado correctamente los datos. Se sustituye la imagen por una imagen por defecto."
            )
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.loadCochesFromZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados desde Zip",
                        header = "Importando datos desde un fichero ZIP con éxito",
                        mensaje = "Se ha importado tus Coches.\nCoches importados: ${viewModel.state.value.coches.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }

    }

    private fun onZipAction() {
        logger.debug { "onZipAction" }
        FileChooser().run {
            title = "Exportar a Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showSaveDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            // Cambiar el cursor a espera
            RoutesManager.activeStage.scene.cursor = WAIT
            viewModel.exportToZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados a Zip",
                        header = "Exportando datos a un fichero ZIP con éxito",
                        mensaje = "Se ha exportado tus Coches completos con imágenes a un fichero Zip.\nCoches exportados: ${viewModel.state.value.coches.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = DEFAULT
        }
    }

}
