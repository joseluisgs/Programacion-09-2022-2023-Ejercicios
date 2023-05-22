package com.example.concesionario.validators

import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.errors.VehicleError
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalDate

const val URL_REGEX = """^https?:\/\/[\w\-]+(\.[\w\-]+)+[/#?]?.*${'$'}"""
const val DATE_REGEX = """\d{4}-\d{2}-\d{2}"""

fun Vehicle.validate(): Result<Vehicle,VehicleError>{
    return when {
        id < 0 -> Err(VehicleError.IdNoValido(id))
        marca.trim().isBlank() -> Err(VehicleError.MarcaNoValida(marca))
        modelo.trim().isBlank() -> Err(VehicleError.ModeloNoValido(modelo))
        fechaMatriculacion.isAfter(LocalDate.now()) -> Err(VehicleError.FechaNoValida(fechaMatriculacion.toString()))
        imagenUrl == null -> Err(VehicleError.ImagenURLNoValida("null"))
        !imagenUrl.matches(Regex(URL_REGEX)) && imagenUrl != "images/default.png" -> Err(VehicleError.ImagenURLNoValida(imagenUrl))
        else -> Ok(this)
    }
}

fun VehicleDto.validate(): Result<VehicleDto, VehicleError>{
    return when {
        id.trim().isBlank() -> Err(VehicleError.IdNoValido(id.toLong()))
        marca.trim().isBlank() -> Err(VehicleError.MarcaNoValida(marca))
        modelo.trim().isBlank() -> Err(VehicleError.ModeloNoValido(modelo))
        motor.trim().isBlank() || !Motor.values().map { it.toString() }.contains(motor) -> Err(VehicleError.MotorNoValido(motor))
        fehcaMatriculacion.trim().isBlank() -> Err(VehicleError.FechaNoValida(fehcaMatriculacion))
        !fehcaMatriculacion.matches(Regex(DATE_REGEX)) -> Err(VehicleError.FechaNoValida(fehcaMatriculacion))
        imagenUrl == null -> Err(VehicleError.ImagenURLNoValida("null"))
        !imagenUrl.matches(Regex(URL_REGEX)) && imagenUrl != "images/default.png" -> Err(VehicleError.ImagenURLNoValida(imagenUrl))
        else -> Ok(this)
    }
}