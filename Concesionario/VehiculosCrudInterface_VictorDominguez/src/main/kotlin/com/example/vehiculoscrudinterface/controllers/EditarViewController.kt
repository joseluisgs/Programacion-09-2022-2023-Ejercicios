package com.example.vehiculoscrudinterface.controllers

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.enums.TipoMotor
import com.example.vehiculoscrudinterface.models.Vehiculo
import com.example.vehiculoscrudinterface.repositories.ConcesionarioRepository
import com.example.vehiculoscrudinterface.repositories.ConcesionarioRepositoryMemory
import com.example.vehiculoscrudinterface.validators.validar
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import mu.KotlinLogging

private val logger = KotlinLogging.logger{}

class EditarViewController(
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
    private lateinit var seleccionMotor: ChoiceBox<String>

    @FXML
    private lateinit var kilometrajeText: TextField

    @FXML
    private lateinit var seleccionFecha: DatePicker

    @FXML
    private lateinit var botonGuardar: Button

    @FXML
    private lateinit var seleccionColor: ChoiceBox<String>

    val motores = listOf<String>(
        "Hibrido",
        "Electrico",
        "Gasolina",
        "Diesel"
    )

    val colores = listOf<String>(
        "Amarillo",
        "Azul",
        "Azul oscuro",
        "Blanco",
        "Morado",
        "Negro",
        "Rojo",
        "Rosa",
        "Verde"
    )

    @FXML
    private fun initialize() {
        logger.debug { "EditarViewController -> Iniciando vista" }
        matricula.text = vehiculo?.id
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream(vehiculo?.imagen))

        marcaText.text = vehiculo?.marca
        modeloText.text = vehiculo?.modelo
        seleccionMotor.items.addAll(motores)
        seleccionMotor.value = vehiculo?.tipoMotor.toString()
        kilometrajeText.text = vehiculo?.km.toString()
        seleccionFecha.value = vehiculo?.fechaMatriculacion
        seleccionColor.items.addAll(colores)
        seleccionColor.value = asignarColor(vehiculo?.imagen)

        botonGuardar.setOnAction { onBotonGuardarClick() }
        seleccionColor.setOnAction { onBotonColoresClick() }
    }

    private fun onBotonColoresClick() {
        logger.debug { "EditarViewController -> Color seleccionado, cambiando imagen" }
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream(asignarImagen(seleccionColor.value)))
    }

    private fun onBotonGuardarClick() {
        logger.debug { "EditarViewController -> Botón Guardar pulsado" }
        if (kilometrajeText.text.toIntOrNull() == null) {
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de error"
                    headerText = "Vehículo no válido"
                    contentText = "El kilometraje debe constar únicamente de números"
                }.showAndWait()
        }

        val vehiculo = Vehiculo(
            matricula.text,
            marcaText.text,
            modeloText.text,
            TipoMotor.valueOf(seleccionMotor.value),
            kilometrajeText.text.toInt(),
            seleccionFecha.value,
            asignarImagen(seleccionColor.value)
        )

        vehiculo.validar().onSuccess {
            logger.debug { "EditarViewController -> Vehículo validado, añadiendo a la base de datos" }
            repository.salvar(vehiculo)
            val stage = botonGuardar.scene.window
            if (stage is Stage) {
                Alert(Alert.AlertType.INFORMATION)
                    .apply {
                        title = "Alerta de información"
                        headerText = "Vehículo editado con éxito"
                    }.showAndWait()
                stage.close()
            }
        }.onFailure { error ->
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de error"
                    headerText = "Vehículo no válido"
                    contentText = error.message
                }.showAndWait()
        }

    }

    private fun asignarImagen(value: String): String {
        return when (value) {
            "Amarillo" -> "icons/cocheAmarillo.png"
            "Azul" -> "icons/cocheAzul.png"
            "Azul oscuro" -> "icons/cocheAzulOscuro.png"
            "Blanco" -> "icons/cocheBlanco.png"
            "Morado" -> "icons/cocheMorado.png"
            "Negro" -> "icons/cocheNegro.png"
            "Rojo" -> "icons/cocheRojo.png"
            "Rosa" -> "icons/cocheRosa.png"
            else -> "icons/cocheVerde.png"
        }
    }

    private fun asignarColor(value: String?): String {
        return when (value) {
            "icons/cocheAmarillo.png" -> "Amarillo"
            "icons/cocheAzul.png" -> "Azul"
            "icons/cocheAzulOscuro.png" -> "Azul oscuro"
            "icons/cocheBlanco.png" -> "Blanco"
            "icons/cocheMorado.png" -> "Morado"
            "icons/cocheNegro.png" -> "Negro"
            "icons/cocheRojo.png" -> "Rojo"
            "icons/cocheRosa.png" -> "Rosa"
            else -> "Verde"
        }
    }
}