package dev.joseluisgs.expedientesacademicos.dto.json

data class CocheDto(
    val id: Long,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val tipoMotor: String,
    val fechaMatriculacion: String,
    val imagen: String,
)
