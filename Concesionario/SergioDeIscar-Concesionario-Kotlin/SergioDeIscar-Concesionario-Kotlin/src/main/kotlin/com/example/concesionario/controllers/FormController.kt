package com.example.concesionario.controllers

import com.example.concesionario.viewmodel.FormViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

class FormController: KoinComponent {
    private val viewModel: FormViewModel by inject()

    @FXML
    private lateinit var textMarca: TextField
    @FXML
    private lateinit var textModelo: TextField
    @FXML
    private lateinit var textImageUrl: TextField

    @FXML
    private lateinit var dateMatriculacion: DatePicker

    @FXML
    private lateinit var comboMotor: ComboBox<String>

    @FXML
    private lateinit var buttonGuardar: Button
    @FXML
    private lateinit var buttonSalir: Button

    @FXML
    private fun initialize() {
        initBindings()
        initEvents()
    }

    private fun initBindings() {
        // ComboBox
        comboMotor.items = FXCollections.observableArrayList(viewModel.state.value.motores)
        comboMotor.selectionModel.selectFirst()

        viewModel.state.addListener { _, oldValue, newValue ->
            if (oldValue == newValue) return@addListener
            textMarca.text = newValue.marca
            textModelo.text = newValue.modelo
            comboMotor.value = newValue.motor
            if (newValue.imagenUrl != "images/default.png")
                textImageUrl.text = newValue.imagenUrl
            else
                textImageUrl.text = ""
            if (newValue.fechaMatriculacion != "")
                dateMatriculacion.value = LocalDate.parse(newValue.fechaMatriculacion)
            else dateMatriculacion.value = null
        }
    }

    private fun initEvents() {
        // ComboBox
        comboMotor.setOnAction {
            viewModel.state.value = viewModel.state.value.copy(motor = comboMotor.value)
        }

        // Button
        buttonSalir.setOnAction {
            buttonSalir.scene.window.hide()
        }

        buttonGuardar.setOnAction {
            if (viewModel.saveAction())
                buttonSalir.scene.window.hide()
        }

        // DatePicker
        dateMatriculacion.setOnAction {
            if (dateMatriculacion.value == null)
                viewModel.state.value = viewModel.state.value.copy(fechaMatriculacion = "")
            else
                viewModel.state.value = viewModel.state.value.copy(fechaMatriculacion = dateMatriculacion.value.toString())
        }

        // TextFields
        textMarca.setOnKeyReleased {
            viewModel.state.value = viewModel.state.value.copy(marca = textMarca.text)
            // Poner el foco al final del texto, ñapa porque como tiene el listener se setea dos veces ._.
            textMarca.positionCaret(textMarca.text.length)
        }
        textModelo.setOnKeyReleased {
            viewModel.state.value = viewModel.state.value.copy(modelo = textModelo.text)
            // Poner el foco al final del texto, ñapa porque como tiene el listener se setea dos veces ._.
            textModelo.positionCaret(textModelo.text.length)
        }
        textImageUrl.setOnKeyReleased {
            viewModel.state.value = viewModel.state.value.copy(imagenUrl = textImageUrl.text)
            // Poner el foco al final del texto, ñapa porque como tiene el listener se setea dos veces ._.
            textImageUrl.positionCaret(textImageUrl.text.length)
        }
    }
}