package com.example.vehiculoscrudinterface.controllers

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.models.Vehiculo
import com.example.vehiculoscrudinterface.repositories.ConcesionarioRepositoryMemory
import com.example.vehiculoscrudinterface.routes.RoutesManager.agregarStage
import com.example.vehiculoscrudinterface.routes.RoutesManager.borrarStage
import com.example.vehiculoscrudinterface.routes.RoutesManager.editarStage
import com.example.vehiculoscrudinterface.services.storage.vehiculo.CsvService
import com.example.vehiculoscrudinterface.services.storage.vehiculo.VehiculoService
import javafx.collections.FXCollections.observableArrayList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import mu.KotlinLogging

private val logger = KotlinLogging.logger{}

class ConcesionarioController(
    private val repository: ConcesionarioRepositoryMemory = ConcesionarioRepositoryMemory(),
    private val service: VehiculoService = CsvService
    ) {

    @FXML
    private lateinit var menuArchivo: Menu

    @FXML
    private lateinit var barraBusqueda: TextField

    @FXML
    private lateinit var listaVehiculos: ListView<Vehiculo>

    @FXML
    private lateinit var matricula: Label

    @FXML
    private lateinit var imagenVehiculo: ImageView

    @FXML
    private lateinit var marcaText: TextField

    @FXML
    private lateinit var modeloText: TextField

    @FXML
    private lateinit var tipoMotorText: TextField

    @FXML
    private lateinit var kilometrajeText: TextField

    @FXML
    private lateinit var fechaText: TextField

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonBorrar: Button

    @FXML
    private fun initialize() {
        logger.debug { "ConcesionarioController -> Iniciando vista principal" }
        listaVehiculos.selectionModel.selectedIndexProperty().addListener { _, _, value ->
            if (value in 0 until listaVehiculos.items.size) {
                rellenarCampos(listaVehiculos.items[value.toInt()])
            } else {
                resetearCampos()
            }
        }

        menuArchivo.items.add(MenuItem("Agregar vehículo"))
        menuArchivo.items.add(MenuItem("Eliminar todos los vehículos"))
        menuArchivo.items.add(MenuItem("Exportar a CSV"))
        menuArchivo.items[0].setOnAction { onBotonAgregarVechiculo() }
        menuArchivo.items[1].setOnAction { onBotonEliminarTodosClick() }
        menuArchivo.items[2].setOnAction { onBotonExportarCsvClick() }
        barraBusqueda.setOnKeyReleased{ ejecutarBusqueda() }
        botonEditar.setOnAction { onBotonEditarClick() }
        botonBorrar.setOnAction { onBotonBorrarClick() }

        // leemos el csv e importamos a la base de datos
        repository.importar(service.importar())
        // sacamos los datos de la base de datos y la añadimos al ListView
        listaVehiculos.items = observableArrayList(repository.buscarTodos())

        resetearCampos()
    }

    private fun onBotonExportarCsvClick() {
        logger.debug { "ConcesionarioController -> Botón Exportar a CSV pulsado" }
        service.exportar(repository.buscarTodos())
        Alert(Alert.AlertType.INFORMATION)
            .apply {
                title = "Aviso de información"
                headerText = "Exportación de datos a CSV"
                contentText = "Archivo CSV generado con éxito en /data"
            }.showAndWait()
    }

    private fun onBotonAgregarVechiculo() {
        logger.debug { "ConcesionarioController -> Botón Agregar vehículo pulsado, abriendo vista Agregar" }
        agregarStage()
    }

    private fun onBotonEliminarTodosClick() {
        logger.debug { "ConcesionarioController -> Botón borrar todos pulsado" }
        val alert = Alert(Alert.AlertType.CONFIRMATION)
            .apply {
                title = "Aviso importante"
                headerText = "ELIMINAR TODOS"
                contentText = "¿Está seguro de eliminar todos los vehículos de la base de datos?"
            }
        alert.buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
        val result = alert.showAndWait()
        if (result.get() == ButtonType.YES) {
            logger.debug { "ConcesionarioController -> Eliminando todos los vehículos" }
            repository.eliminarTodos()
            actualizarLista()
            Alert(Alert.AlertType.INFORMATION)
                .apply {
                    title = "Aviso informativo"
                    headerText = "Proceso finalizado"
                    contentText = "Se han eliminado todos los vehículos de la base de datos"
                }.showAndWait()
        } else {
            Alert(Alert.AlertType.INFORMATION)
                .apply {
                    title = "Aviso informativo"
                    headerText = "Proceso cancelado"
                    contentText = "STOP: Eliminar todos"
                }.showAndWait()
        }
    }

    private fun rellenarCampos(item: Vehiculo) {
        logger.debug { "ConcesionarioController -> Rellenando campos" }
        matricula.text = item.id
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream(item.imagen))
        marcaText.text = item.marca
        modeloText.text = item.modelo
        tipoMotorText.text = item.tipoMotor.toString()
        kilometrajeText.text = item.km.toString()
        fechaText.text = item.fechaMatriculacion.toString()
        botonBorrar.isDisable = false
        botonEditar.isDisable = false
    }

    private fun resetearCampos() {
        logger.debug { "ConcesionarioController -> Reseteando campos" }
        matricula.text = "       "
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/coche.png"))
        marcaText.text = ""
        modeloText.text = ""
        tipoMotorText.text = ""
        kilometrajeText.text = ""
        fechaText.text = ""
        botonBorrar.isDisable = true
        botonEditar.isDisable = true
    }

    private fun ejecutarBusqueda() {
        logger.debug { "ConcesionarioController -> Ejecutando búsqueda" }
        val vehiculos = repository.buscarTodos().filter { it.id.contains(barraBusqueda.text.uppercase()) }
        listaVehiculos.items = observableArrayList(vehiculos)
    }

    private fun onBotonEditarClick() {
        logger.debug { "ConcesionarioController -> Botón Editar pulsado, abriendo vista Editar" }
        editarStage(matricula.text)
    }

    private fun onBotonBorrarClick() {
        logger.debug { "ConcesionarioController -> Botón Borrar pulsado, abriendo vista Borrar" }
        borrarStage(matricula.text)
    }

    fun actualizarLista() {
        logger.debug { "ConcesionarioController -> Actualizando ListView" }
        listaVehiculos.items = observableArrayList(repository.buscarTodos())
    }

}