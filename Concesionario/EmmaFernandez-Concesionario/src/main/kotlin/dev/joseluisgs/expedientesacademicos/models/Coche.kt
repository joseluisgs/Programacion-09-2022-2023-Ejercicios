package dev.joseluisgs.expedientesacademicos.models

import java.time.LocalDate

data class Coche(
    val id: Long = NEW_COCHE,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val tipoMotor: TipoMotor,
    val fechaMatriculacion: LocalDate,
    val imagen: String
) {
    companion object {
        const val NEW_COCHE = -1L
    }
    enum class TipoMotor {
        GASOLINA, DIESEL, HIBRIDO, ELECTRICO
    }

    fun isNewCoche(): Boolean = id == NEW_COCHE
}
