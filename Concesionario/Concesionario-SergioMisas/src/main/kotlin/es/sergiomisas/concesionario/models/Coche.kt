package es.sergiomisas.concesionario.models

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
    enum class TipoMotor {
        TODOS, DIESEL, GASOLINA, ELECTRICO, HIBRIDO
    }

    companion object {
        const val NEW_COCHE = -1L
    }

    val marcaYModelo: String
        get() = "$marca $modelo"

    fun isNewCoche(): Boolean = id == NEW_COCHE

}
