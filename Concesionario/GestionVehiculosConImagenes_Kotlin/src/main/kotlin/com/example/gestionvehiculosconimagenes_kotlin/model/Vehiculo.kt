package com.example.gestionvehiculosconimagenes_kotlin.model

import java.time.LocalDate

data class Vehiculo(
    val id: Long = -1L,
    val matricula: String,
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

    override fun equals(other: Any?): Boolean {
        //Con esta función compara vehículos sin tener en cuenta la foto asociada, podría tener en cuenta solo el id y la maticula, pero bueno.
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehiculo

        if (id != other.id) return false
        if (matricula != other.matricula) return false
        if (marca != other.marca) return false
        if (modelo != other.modelo) return false
        if (tipoMotor != other.tipoMotor) return false
        if (km != other.km) return false
        if (fechaMatriculacion != other.fechaMatriculacion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + matricula.hashCode()
        result = 31 * result + marca.hashCode()
        result = 31 * result + modelo.hashCode()
        result = 31 * result + tipoMotor.hashCode()
        result = 31 * result + km.hashCode()
        result = 31 * result + fechaMatriculacion.hashCode()
        return result
    }


}

enum class TipoMotor(val imagePath: String){
    CUALQUIERA("images/not_found.png"), ELECTRICO("images/electric.png"), GASOLINA("images/gasoline.png"), DIESEL("images/diesel.png"), HIBRIDO("images/hybrid.png"), OTRO("images/not_found.png")
}