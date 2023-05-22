package com.jczhang.jczhangcocheskotlinjavafx.controller

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.jczhang.jczhangcocheskotlinjavafx.routes.RoutesManager
import com.jczhang.jczhangcocheskotlinjavafx.viewmodel.CochesViewModel
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import validator.validate
import validator.validate2


class EditItemViewController : KoinComponent {

    private val logger = KotlinLogging.logger {  }

    lateinit var matriculationMostrar: DatePicker
    lateinit var kmMostrar: TextField
    lateinit var tipoMotorMostrar: TextField
    lateinit var marcaMostrar: TextField
    lateinit var modeloMostrar: TextField
    lateinit var idMostrar: TextField

    private val viewModel: CochesViewModel by inject()

    fun onSaveAction() {
        logger.debug { "editando" }
        validate2(idMostrar, marcaMostrar, modeloMostrar, kmMostrar, tipoMotorMostrar, matriculationMostrar ).onSuccess {
            viewModel.editCar(it)
            viewModel.loadFromDatabase()
            RoutesManager.activeStage.close()
        }.onFailure {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.apply {
                title = "HEY! TE HAS EQUIVOCADO"
                contentText = it.message
                onCleanAction()
                alert.showAndWait()
            }
        }

    }

    fun showData() {
        idMostrar.text = viewModel.state.cocheSeleccionado.id.value.toString()
        tipoMotorMostrar.text = viewModel.state.cocheSeleccionado.tipoMotor.value.toString()
        kmMostrar.text = viewModel.state.cocheSeleccionado.km.value.toString()
        marcaMostrar.text = viewModel.state.cocheSeleccionado.marca.value.toString()
        modeloMostrar.text = viewModel.state.cocheSeleccionado.modelo.value.toString()
        idMostrar.text = viewModel.state.cocheSeleccionado.id.value.toString()
        matriculationMostrar.value =  viewModel.state.cocheSeleccionado.matriculacion.value
    }

    fun onCleanAction() {
        matriculationMostrar.value = null
        kmMostrar.text = ""
        tipoMotorMostrar.text = ""
        marcaMostrar.text = ""
        modeloMostrar.text = ""
        idMostrar.text = ""
    }

    fun onCancelAction() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.apply {
            headerText = "¿Está seguro de que desea salir?"
            contentText = "Se perderán todos los datos no guardados."

        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                RoutesManager.activeStage.close()
            }
        }
    }


}
