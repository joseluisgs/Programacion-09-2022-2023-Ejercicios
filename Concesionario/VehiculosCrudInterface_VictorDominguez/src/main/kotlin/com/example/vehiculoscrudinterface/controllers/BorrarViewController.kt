package com.example.vehiculoscrudinterface.controllers

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.repositories.ConcesionarioRepository
import com.example.vehiculoscrudinterface.repositories.ConcesionarioRepositoryMemory
import com.example.vehiculoscrudinterface.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import mu.KotlinLogging
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger{}

class BorrarViewController(
    val id: String,
    val repository: ConcesionarioRepository = ConcesionarioRepositoryMemory()
) {

    val vehiculo = repository.buscarPorId(id)

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
    private lateinit var botonBorrar: Button

    @FXML
    private fun initialize() {
        logger.debug { "BorrarViewController -> Iniciando vista" }
        matricula.text = vehiculo?.id
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream(vehiculo?.imagen))
        marcaText.text = vehiculo?.marca
        modeloText.text = vehiculo?.modelo
        tipoMotorText.text = vehiculo?.tipoMotor.toString()
        kilometrajeText.text = vehiculo?.km.toString()
        fechaText.text = vehiculo?.fechaMatriculacion.toString()
        botonBorrar.setOnAction { onBotonBorrarClick() }
    }

    private fun onBotonBorrarClick() {
        logger.debug { "BorrarViewController -> Botón Borrar pulsado" }
        val alert = Alert(Alert.AlertType.CONFIRMATION)
            .apply {
                title = "Aviso importante"
                headerText = "Eliminar vehículo"
                contentText = "¿Está seguro de eliminar este vehículo de la base de datos?"
            }
        alert.buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
        val result = alert.showAndWait()
        if (result.get() == ButtonType.YES) {
            logger.debug { "BorrarViewController -> Eliminando vehículo" }
            repository.eliminarPorId(id)
            val stage = botonBorrar.scene.window
            if (stage is Stage) {
                Alert(Alert.AlertType.INFORMATION)
                    .apply {
                        title = "Alerta de información"
                        headerText = "Vehículo borrado con éxito"
                    }.showAndWait()
                stage.close()
            }
        } else {
            Alert(Alert.AlertType.INFORMATION)
                .apply {
                    title = "Aviso informativo"
                    headerText = "Proceso cancelado"
                }.showAndWait()
        }



    }

}