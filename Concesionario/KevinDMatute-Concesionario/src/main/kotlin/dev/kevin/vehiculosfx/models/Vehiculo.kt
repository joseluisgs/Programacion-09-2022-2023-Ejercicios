package dev.kevin.vehiculosfx.models

import dev.kevin.vehiculosfx.enums.tipoMotor
import java.time.LocalDate

class Vehiculo(
    val id: Long = NEW_VEHICULO,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val motor: String,
    val km: Int,
    val fechaMatriculacion: LocalDate,
    val imagen: String
) {

    companion object {
        const val NEW_VEHICULO = -1L
    }

    fun isNewVehiculo(): Boolean = id == NEW_VEHICULO

}