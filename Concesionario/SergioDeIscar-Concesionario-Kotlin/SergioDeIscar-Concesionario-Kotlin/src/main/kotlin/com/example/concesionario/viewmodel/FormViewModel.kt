package com.example.concesionario.viewmodel

import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.mappers.toClass
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import com.example.concesionario.repositories.vehicle.VehicleRepository
import com.example.concesionario.states.ActionType
import com.example.concesionario.states.FormState
import com.example.concesionario.validators.validate
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.get
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Alert
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FormViewModel: KoinComponent {
    private val repository: VehicleRepository by inject()
    private val vehicleViewModel: VehicleViewModel by inject()

    val state: ObjectProperty<FormState> = SimpleObjectProperty(FormState())

    init {
        vehicleViewModel.state.addListener { _, _, _ ->
            changeStateActionType()
        }
        loadMotores()
    }

    private fun changeStateActionType() {
        if (vehicleViewModel.state.value.actionType == ActionType.EDIT) {
            state.value = state.value.copy(
                id = vehicleViewModel.state.value.id,
                marca = vehicleViewModel.state.value.marca,
                modelo = vehicleViewModel.state.value.modelo,
                motor = vehicleViewModel.state.value.motor,
                fechaMatriculacion = vehicleViewModel.state.value.fechaMatriculacion,
                imagenUrl = vehicleViewModel.state.value.imagenUrl
            )
        } else {
            state.value = state.value.copy(
                id = "0",
                marca = "",
                modelo = "",
                motor = "",
                fechaMatriculacion = "",
                imagenUrl = ""
            )
        }
    }

    private fun loadMotores() {
        state.value = state.value.copy(
            motores = listOf(
                "",
                Motor.DIESEL.value,
                Motor.GASOLINA.value,
                Motor.HIBRIDO.value,
                Motor.ELECTRICO.value
            )
        )
    }

    fun saveAction(): Boolean {
        val vehicle = VehicleDto(
            id = state.value.id,
            marca = state.value.marca,
            modelo = state.value.modelo,
            motor = state.value.motor,
            fehcaMatriculacion = state.value.fechaMatriculacion,
            imagenUrl = state.value.imagenUrl
        ).validate().onFailure {
            Alert(Alert.AlertType.ERROR).apply {
                this.title = "Datos incorrectos"
                this.headerText = "Los datos introducidos no son correctos"
                this.contentText = it.message
            }.showAndWait()
            return false
        }.andThen { it.toClass().validate() }.onFailure {
            Alert(Alert.AlertType.ERROR).apply {
                this.title = "Datos incorrectos"
                this.headerText = "Los datos introducidos no son correctos"
                this.contentText = it.message
            }.showAndWait()
            return false
        }.get()!!

        return when(vehicleViewModel.state.value.actionType){
            ActionType.EDIT -> {
                repoVehicle(
                    vehicle = vehicle,
                    title = "Editar vehículo",
                    headerText = "Editar vehículo",
                    contentText = "El vehículo se ha editado correctamente",
                    titleError = "Error al editar",
                    headerTextError = "Error al editar el vehículo",
                    contentTextError = "Ha ocurrido un error al editar el vehículo: "
                )
            }
            ActionType.NEW -> {
                repoVehicle(
                    vehicle = vehicle,
                    title = "Nuevo vehículo",
                    headerText = "Nuevo vehículo",
                    contentText = "El vehículo se ha guardado correctamente",
                    titleError = "Error al guardar",
                    headerTextError = "Error al guardar el vehículo",
                    contentTextError = "Ha ocurrido un error al guardar el vehículo: "
                )
            }
        }
    }

    private fun repoVehicle(
        vehicle: Vehicle,
        title: String,
        headerText: String,
        contentText: String,
        titleError: String,
        headerTextError: String,
        contentTextError: String
    ): Boolean {
        repository.save(
            vehicle
        ).mapBoth(
            success = {
                Alert(Alert.AlertType.INFORMATION).apply {
                    this.title = title
                    this.headerText = headerText
                    this.contentText = contentText
                }.showAndWait()
                vehicleViewModel.updateViewList()
                return true
            },
            failure = {
                Alert(Alert.AlertType.ERROR).apply {
                    this.title = titleError
                    this.headerText = headerTextError
                    this.contentText = contentTextError + it.message
                }.showAndWait()
                return false
            }
        )
    }
}