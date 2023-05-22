package es.sergiomisas.concesionario.mappers

import database.CocheEntity
import es.sergiomisas.concesionario.dto.json.CocheDto
import es.sergiomisas.concesionario.models.Coche
import es.sergiomisas.concesionario.viewmodels.ConcesionarioViewModel.CocheFormulario
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
        tipoMotor = es.sergiomisas.concesionario.models.Coche.TipoMotor.valueOf(tipoMotor),
        fechaMatriculacion = fechaMatriculacion,
        imagen = imagen.url ?: "sin-imagen.png"
    )
}
