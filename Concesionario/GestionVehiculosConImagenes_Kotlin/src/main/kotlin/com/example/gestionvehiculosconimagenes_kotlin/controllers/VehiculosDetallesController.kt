package com.example.gestionvehiculosconimagenes_kotlin.controllers

import com.example.gestionvehiculosconimagenes_kotlin.error.VehiculoError
import com.example.gestionvehiculosconimagenes_kotlin.mapper.getTipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.routes.RoutesManager
import com.example.gestionvehiculosconimagenes_kotlin.utils.getResourceAsStream
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.TipoOperacion
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.VehiculoReference
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.ViewModel
import com.github.michaelbull.result.*
import javafx.collections.FXCollections
import javafx.event.Event
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

class VehiculosDetallesController: KoinComponent {
    //named("MySQL")
    val viewModel: ViewModel by inject()

    @FXML
    private lateinit var botonCancelar: Button

    @FXML
    private lateinit var botonLimpiar: Button

    @FXML
    private lateinit var botonAceptar: Button

    @FXML
    private lateinit var fechaMatriculacion: DatePicker

    @FXML
    private lateinit var kilometros: TextField

    @FXML
    private lateinit var tipoMotor: ChoiceBox<String>

    @FXML
    private lateinit var modelo: TextField

    @FXML
    private lateinit var marca: TextField

    @FXML
    private lateinit var imagenVehiculo: ImageView

    @FXML
    private lateinit var matricula: TextField

    @FXML
    private lateinit var idVehiculo: TextField

    private var imageFile: File? = null

    @FXML
    fun initialize(){

        //Para que en el campo de la fecha no se pueda mas que seleccionar con el picker
        fechaMatriculacion.isEditable = false

        initBinds()

        initEvents()
    }

    private fun initEvents() {
        logger.debug { "Asociamos todos los eventos necesarios a los elementos de la vista." }

        botonLimpiar.setOnAction {
            onClickLimpiarAction()
        }

        botonCancelar.setOnAction {
            onClickCancelarOperacion(it)
        }

        botonAceptar.setOnAction {
            onClickAceptarOperacion()
        }

        imagenVehiculo.setOnMouseClicked {
            onMouseClickSelectImage()
        }
    }

    private fun initBinds() {
        logger.debug { "Iniciamos todos los bindings que sean necesarios en la aplicación" }
        //Independiente del tipo de operación, cargo las posibles opciones del choice box
        //Le quito la opción CUALQUIERA ya que no deseo que sea un posible opción
        tipoMotor.items = FXCollections.observableArrayList(viewModel.state.value.comboBoxOptions.filter { it != TipoMotor.CUALQUIERA.toString() })

        if (viewModel.state.value.tipoOperacion == TipoOperacion.EDITAR) {
            idVehiculo.text =
                if(viewModel.state.value.vehiculoReference.id == Vehiculo.VEHICULO_ID) "" else viewModel.state.value.vehiculoReference.id.toString()
            matricula.text = viewModel.state.value.vehiculoReference.matricula
            marca.text = viewModel.state.value.vehiculoReference.marca
            modelo.text = viewModel.state.value.vehiculoReference.modelo
            kilometros.text = viewModel.state.value.vehiculoReference.km.toString()
            fechaMatriculacion.value = viewModel.state.value.vehiculoReference.fechaMatriculacion
            imagenVehiculo.image = viewModel.state.value.vehiculoReference.foto
            //Si hemos elegido editar, en el combo box por defecto nos sale la opción que había antes
            tipoMotor.selectionModel.select(viewModel.state.value.vehiculoReference.tipoMotor.toString())
        }else{
            //Si hemos elegido añadir, en el combo box empezmos por defecto en la primera opción
            tipoMotor.selectionModel.selectLast()
        }
    }

    private fun onMouseClickSelectImage() {
        logger.debug { "Se abre un menú de selección de imagenes para el usuario" }
        FileChooser().run {
            title = "Seleccione la imagen que desea añadir al vehículo"
            extensionFilters.addAll(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            imagenVehiculo.image = Image(it.toURI().toString())
            imageFile = it
        }
    }

    private fun onClickAceptarOperacion() {
        if(viewModel.state.value.tipoOperacion == TipoOperacion.AÑADIR){
            logger.debug { "Llevamos a cabo la opción de añadir un nuevo vehículo" }
            generatedVehiculo()
                .andThen {
                    viewModel.saveVehiculo(it)
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Vehículo creado"
                        headerText = "El vehículo creado se ha guardado correctamente"
                        contentText = "El vehículo ${it.marcaModelo} fue guardado"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Vehículo no creado"
                        headerText = "El vehículo no pudo ser creado"
                        contentText = it.message
                    }.showAndWait()
                    //Estos return son para que no salga de una si algo ha fallado
                    return
                }
        }else{
            logger.debug { "Llevamos a cabo la opción de editar un vehículo" }
            generatedVehiculo()
                .andThen {
                    viewModel.updateVehiculo(it)
                }
                .onSuccess {
                    Alert(Alert.AlertType.CONFIRMATION).apply {
                        title = "Vehículo editado"
                        headerText = "El vehículo editado se ha guardado correctamente"
                        contentText = "El vehículo ${it.marcaModelo} fue editado correctamente"
                    }.showAndWait()
                }
                .onFailure {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Vehículo no editado"
                        headerText = "El vehículo no pudo ser editado"
                        contentText = it.message
                    }.showAndWait()
                    //Estos return son para que no salga de una si algo ha fallado
                    return
                }
        }
        RoutesManager.activeStage.close()
    }

    private fun generatedVehiculo(): Result<VehiculoReference, VehiculoError> {
        logger.debug { "Generamos un vehículo según los campos que tenemos, tras validar todo" }


        require(!viewModel.state.value.vehiculos.filter { it.id != (idVehiculo.text.toLongOrNull() ?: Vehiculo.VEHICULO_ID) }.map { it.matricula }.contains(matricula.text)){
            return  Err(VehiculoError.MatriculaYaExiste(matricula.text))
        }
        val regexMatricula = Regex("[A-Z]{4}[0-9]{3}")
        require(matricula.text.matches(regexMatricula)){
            return Err(VehiculoError.MatriculaNoValida(matricula.text))
        }
        require(marca.text.isNotEmpty()){
            return Err(VehiculoError.MarcaNoValida(marca.text))
        }
        require(modelo.text.isNotEmpty()){
            return Err(VehiculoError.ModeloNoValido(modelo.text))
        }
        val km = kilometros.text.toDoubleOrNull()
        require(km != null){
            return Err(VehiculoError.KilometroNoValido(kilometros.text))
        }
        require(km >= 0.0){
            return Err(VehiculoError.KilometroNoValido(km.toString()))
        }
        if(fechaMatriculacion.value.isAfter(LocalDate.now())){
            return Err(VehiculoError.FechaMatriculacionNoValida(fechaMatriculacion.value))
        }
        return Ok(VehiculoReference(
            id = idVehiculo.text.toLongOrNull()?:Vehiculo.VEHICULO_ID,
            matricula = matricula.text,
            marca = marca.text,
            modelo = modelo.text,
            tipoMotor = tipoMotor.selectionModel.selectedItem.getTipoMotor(),
            km = kilometros.text.toDoubleOrNull()?:0.0,
            fechaMatriculacion = fechaMatriculacion.value,
            foto = imagenVehiculo.image,
            fotoFile = imageFile
        ))
    }

    fun onClickCancelarOperacion(event: Event) {
        val operacion = viewModel.state.value.tipoOperacion.toString().lowercase()
        logger.debug { "Se cancela la operación $operacion que estabamos realizando" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "¿Quieres salir de la operación $operacion?"
            headerText = "¿Seguro que desea proseguir?"
            contentText = "Estás apunto de salir de la $operacion sobre vehículos."
        }.showAndWait().ifPresent {
            if(it == ButtonType.OK){
                //El activeStage es la ventana modal, al cerrarlo vuelvo al mainStage
                RoutesManager.activeStage.close()
            }else{
                event.consume()
            }
        }
    }

    private fun onClickLimpiarAction() {
        logger.debug { "Limpiamos los datos de todos los campos" }
        imagenVehiculo.image = Image(getResourceAsStream(TipoMotor.OTRO.imagePath))
        matricula.text = ""
        marca.text = ""
        modelo.text = ""
        tipoMotor.selectionModel.selectLast()
        kilometros.text = ""
        fechaMatriculacion.value = null
    }

}