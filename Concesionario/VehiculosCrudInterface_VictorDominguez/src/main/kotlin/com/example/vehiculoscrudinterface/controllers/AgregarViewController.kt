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
import kotlin.math.log

private val logger = KotlinLogging.logger{}

class AgregarViewController(
    val repository: ConcesionarioRepository = ConcesionarioRepositoryMemory()
) {

    @FXML
    private lateinit var matriculaText: TextField

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
    private lateinit var botonComprobar: Button

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
        logger.debug { "AgregarViewController -> Iniciando vista" }
        botonGuardar.isDisable = true
        matriculaText.text = ""
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/coche.png"))
        marcaText.text = ""
        modeloText.text = ""
        kilometrajeText.text = ""

        seleccionMotor.items.addAll(motores)
        seleccionColor.items.addAll(colores)

        botonGuardar.setOnAction { onBotonGuardarClick() }
        botonComprobar.setOnAction { onBotonComprobarClick() }
        seleccionColor.setOnAction { onBotonColoresClick() }

        matriculaText.setOnKeyReleased { botonGuardar.isDisable = true }
        marcaText.setOnKeyReleased { botonGuardar.isDisable = true }
        modeloText.setOnKeyReleased { botonGuardar.isDisable = true }
        kilometrajeText.setOnKeyReleased { botonGuardar.isDisable = true }
        seleccionFecha.setOnAction { botonGuardar.isDisable = true }
    }

    private fun onBotonColoresClick() {
        logger.debug { "AgregarViewController -> Color seleccionado, cambiando imagen" }
        imagenVehiculo.image = Image(ConcesionarioApplication::class.java.getResourceAsStream(asignarImagen(seleccionColor.value)))
    }

    private fun onBotonComprobarClick() {
        logger.debug { "AgregarViewController -> Botón Comprobar pulsado" }
        if (seleccionMotor.value == null) {
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de campo no válido"
                    headerText = "Campo no válido"
                    contentText = "Se debe elegir un tipo de motor"
                }.showAndWait()
        }
        if (kilometrajeText.text.toIntOrNull() == null) {
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de campo no válido"
                    headerText = "Campo no válido"
                    contentText = "El kilometraje debe constar únicamente de números"
                }.showAndWait()
        }
        if (seleccionFecha.value == null) {
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de campo no válido"
                    headerText = "Campo no válido"
                    contentText = "Se debe elegir una fecha"
                }.showAndWait()
        }
        if (seleccionColor.value == null) {
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de campo no válido"
                    headerText = "Campo no válido"
                    contentText = "Se debe elegir un color"
                }.showAndWait()
        }

        val vehiculo = Vehiculo(
            matriculaText.text.uppercase(),
            marcaText.text,
            modeloText.text,
            TipoMotor.valueOf(seleccionMotor.value),
            kilometrajeText.text.toInt(),
            seleccionFecha.value,
            asignarImagen(seleccionColor.value)
        )

        vehiculo.validar().onSuccess {
            logger.debug { "AgregarViewController -> Vehículo validado" }
            Alert(Alert.AlertType.INFORMATION)
                .apply {
                    title = "Alerta de confirmación"
                    headerText = "Campos validados"
                }.showAndWait()
            botonGuardar.isDisable = false
        }.onFailure { error ->
            Alert(Alert.AlertType.ERROR)
                .apply {
                    title = "Alerta de error"
                    headerText = "Vehículo no válido"
                    contentText = error.message
                }.showAndWait()
        }
    }

    private fun onBotonGuardarClick() {
        logger.debug { "AgregarViewController -> Botón Guardar pulsado, agregando vehículo a la base de datos" }
        repository.salvar(
            Vehiculo(
                matriculaText.text.uppercase(),
                marcaText.text,
                modeloText.text,
                TipoMotor.valueOf(seleccionMotor.value),
                kilometrajeText.text.toInt(),
                seleccionFecha.value,
                asignarImagen(seleccionColor.value)
            )
        )
        val stage = botonGuardar.scene.window
        if (stage is Stage) {
            Alert(Alert.AlertType.CONFIRMATION)
                .apply {
                    title = "Alerta de confirmación"
                    headerText = "Vehículo agregado con éxito"
                }.showAndWait()
            stage.close()
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

//    private fun asignarColor(value: String?): String {
//        return when (value) {
//            "icons/cocheAmarillo.png" -> "Amarillo"
//            "icons/cocheAzul.png" -> "Azul"
//            "icons/cocheAzulOscuro.png" -> "Azul oscuro"
//            "icons/cocheBlanco.png" -> "Blanco"
//            "icons/cocheMorado.png" -> "Morado"
//            "icons/cocheNegro.png" -> "Negro"
//            "icons/cocheRojo.png" -> "Rojo"
//            "icons/cocheRosa.png" -> "Rosa"
//            else -> "Verde"
//        }
//    }

}