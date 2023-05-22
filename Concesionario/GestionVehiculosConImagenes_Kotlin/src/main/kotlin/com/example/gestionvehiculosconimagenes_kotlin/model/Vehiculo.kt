package com.example.gestionvehiculosconimagenes_kotlin.model

import java.time.LocalDate

data class Vehiculo(
    val id: Long = -1L,
    val marca: String,
    val modelo: String,
    val tipoMotor: TipoMotor,
    val km: Double,
    val fechaMatriculacion: LocalDate = LocalDate.now(),
    val foto: String = tipoMotor.imagePath
){
    companion object{
        const val VEHICULO_ID = -1L
    }

    val marcaModelo get() = marca+"-"+modelo

    val tipoMotorText get() = tipoMotor.toString()
}

enum class TipoMotor(val imagePath: String){
    CUALQUIERA("images/not_found.png"), OTRO("images/not_found.png"), ELECTRICO("images/electric.png"), GASOLINA("images/gasoline.png"), DIESEL("images/diesel.png"), HIBRIDO("images/hybrid.png")
}