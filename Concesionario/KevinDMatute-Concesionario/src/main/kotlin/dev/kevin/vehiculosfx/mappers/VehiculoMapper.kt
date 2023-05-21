package dev.kevin.vehiculosfx.mappers

import database.VehiculoEntity
import dev.kevin.vehiculosfx.enums.tipoMotor
import dev.kevin.vehiculosfx.models.Vehiculo
import dev.kevin.vehiculosfx.viewmodels.VehiculoViewModel
import java.time.LocalDate

fun VehiculoEntity.toModel(): Vehiculo{
    return Vehiculo(
        id,
        marca,
        matricula,
        modelo,
        motor,
        km.toInt(),
        LocalDate.parse(fechaMatriculacion),
        imagen
    )
}

fun VehiculoViewModel.VehiculoState.toModel(): Vehiculo{
    return Vehiculo(
        id,
        marca,
        matricula,
        modelo,
        motor,
        km,
        fechaMatriculacion,
        imagen = imagen.url ?: "sin-imagen.png"
    )
}



