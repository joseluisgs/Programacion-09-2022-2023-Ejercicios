package dev.sergiosf.concesionario.mappers

import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.viewmodels.VehiculosViewModel

fun VehiculosViewModel.VehiculoFormulario.toModel(): Vehiculo {
    return Vehiculo(
        id = id,
        marca = marca,
        modelo = modelo,
        matricula = matricula,
        tipoVehiculo = tipoVehiculo,
        fechaMatriculation = fechaMatriculation,
        image = imagen.url ?: "images/sin-imagen.jpg"
    )
}