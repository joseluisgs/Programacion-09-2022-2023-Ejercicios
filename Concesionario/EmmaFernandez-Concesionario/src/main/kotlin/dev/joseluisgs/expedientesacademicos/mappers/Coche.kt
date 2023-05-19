package dev.joseluisgs.expedientesacademicos.mappers

import database.CocheEntity
import dev.joseluisgs.expedientesacademicos.dto.json.CocheDto
import dev.joseluisgs.expedientesacademicos.models.Coche
import dev.joseluisgs.expedientesacademicos.viewmodels.ConcesionarioViewModel.CocheFormulario
import java.time.LocalDate

fun CocheDto.toModel(): Coche {
    return Coche(
        id,
        matricula,
        marca,
        modelo,
        Coche.TipoMotor.valueOf(tipoMotor),
        LocalDate.parse(fechaMatriculacion),
        imagen
    )
}

fun List<CocheDto>.toModel(): List<Coche> {
    return map { it.toModel() }
}

fun Coche.toDto(): CocheDto {
    return CocheDto(
        id,
        matricula,
        marca,
        modelo,
        tipoMotor.name,
        fechaMatriculacion.toString(),
        imagen
    )
}

fun List<Coche>.toDto(): List<CocheDto> {
    return map { it.toDto() }
}

fun CocheEntity.toModel(): Coche {
    return Coche(
        id,
        matricula,
        marca,
        modelo,
        Coche.TipoMotor.valueOf(tipoMotor),
        LocalDate.parse(fechaMatriculacion),
        imagen
    )
}

fun CocheFormulario.toModel(): Coche {
    return Coche(
        id = numero,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        tipoMotor = Coche.TipoMotor.valueOf(tipoMotor),
        fechaMatriculacion = fechaMatriculacion,
        imagen = imagen.url ?: "sin-imagen.png"
    )
}
