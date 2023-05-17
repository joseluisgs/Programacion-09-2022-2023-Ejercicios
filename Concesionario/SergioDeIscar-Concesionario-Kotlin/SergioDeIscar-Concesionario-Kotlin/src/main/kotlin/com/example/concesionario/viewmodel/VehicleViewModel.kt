package com.example.concesionario.viewmodel

import com.example.concesionario.mappers.toDto
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import com.example.concesionario.repositories.vehicle.VehicleRepository
import com.example.concesionario.services.storage.vehicle.VehicleStorage
import javafx.beans.property.SimpleObjectProperty
import com.example.concesionario.states.VehicleState
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.beans.property.ObjectProperty
import javafx.scene.control.Alert
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VehicleViewModel: KoinComponent {
    private val repository:VehicleRepository by inject()
    private val storage:VehicleStorage by inject()

    val state: ObjectProperty<VehicleState> = SimpleObjectProperty(VehicleState())
    init {
        updateViewList()
        loadMotores()
    }

    fun updateViewList() {
        state.value = state.value.copy(
            vehicles = repository.findAll().toList()
        )
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

    fun updateVehicleSelected(it: Vehicle) {
        val dto = it.toDto()
        state.value = state.value.copy(
            id = dto.id,
            marca = dto.marca,
            modelo = dto.modelo,
            motor = dto.motor,
            fechaMatriculacion = dto.fehcaMatriculacion,
            imagenUrl = dto.imagenUrl ?: "images/default.png"
        )
    }

    fun vehiclesFilteredList(value: String?, textFilter: String): List<Vehicle> {
        return state.value.vehicles.filter {
            when(value){
                Motor.DIESEL.value -> it.motor == Motor.DIESEL
                Motor.GASOLINA.value -> it.motor == Motor.GASOLINA
                Motor.HIBRIDO.value -> it.motor == Motor.HIBRIDO
                Motor.ELECTRICO.value -> it.motor == Motor.ELECTRICO
                else -> true
            }
        }.filter {
            it.marca.contains(textFilter, true) ||
            it.modelo.contains(textFilter, true)
        }
    }

    fun importVehiclesFromJson() {
        storage.loadAll().onSuccess {
            repository.saveAll(it)
            updateViewList()
        }.onFailure {
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error"
                headerText = "Error al importar los vehiculos"
                contentText = it.message
            }.showAndWait()
        }
    }

    fun importVehiclesFromCsv() {
        println("Importar vehiculos desde CSV")
    }

    fun exportVehiclesToJson() {
        storage.saveAll(repository.findAll().toList()).mapBoth(
            success = {
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Información"
                    headerText = "Vehiculos exportados correctamente"
                    contentText = "Se han exportado ${it.size} vehiculos"
                }.showAndWait()
                updateViewList()
            },
            failure = {
                Alert(Alert.AlertType.ERROR).apply {
                    title = "Error"
                    headerText = "Error al exportar los vehiculos"
                    contentText = it.message
                }.showAndWait()
            }
        )
    }

    fun exportVehiclesToCsv() {
        println("Exportar vehiculos a CSV")
    }

    fun deleteVehicle() {
        repository.deleteById(state.value.id.toLong()).mapBoth(
            success = {
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Información"
                    headerText = "Vehiculo eliminado correctamente"
                    contentText = "Se ha eliminado el vehiculo con id ${state.value.id}"
                }.showAndWait()
                updateViewList()
            },
            failure = {
                Alert(Alert.AlertType.ERROR).apply {
                    title = "Error"
                    headerText = "Error al eliminar el vehiculo"
                    contentText = it.message
                }.showAndWait()
            }
        )
    }
}