package dev.sergiosf.concesionario.models

import dev.sergiosf.concesionario.viewmodels.VehiculosViewModel
import java.time.LocalDate

data class Vehiculo(
    val id: Long,
    val marca: String,
    val modelo: String,
    val matricula: String,
    val tipoVehiculo: TipoVehiculo,
    val fechaMatriculation: LocalDate = LocalDate.now(),
    val image: String
    ) {
    companion object {
        const val NEW_VEHICULO = -1L
    }

    fun isNewVehiculo(): Boolean = id == NEW_VEHICULO

    enum class TipoVehiculo(val value: String){
        NONE(""), GASOLINA("Gasolina"), DIESEL("Diesel"), ELECTRICO("Elelctrico"), HIBRIDO("Hibrido")
    }
}
