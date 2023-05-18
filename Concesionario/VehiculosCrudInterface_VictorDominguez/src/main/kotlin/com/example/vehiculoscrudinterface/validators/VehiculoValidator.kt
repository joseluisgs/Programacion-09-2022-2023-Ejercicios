package com.example.vehiculoscrudinterface.validators

import com.example.vehiculoscrudinterface.errors.VehiculoError
import com.example.vehiculoscrudinterface.models.Vehiculo
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.time.LocalDate

private val matriculaRegex = Regex("^[0-9]{4}[A-Z|a-z]{3}$")

fun Vehiculo.validar(): Result<Vehiculo, VehiculoError> {
    require(this.id.isNotBlank()) { return Err(VehiculoError.VehiculoNoValidoError("El campo matrícula no puede estar vacío")) }
    require(this.id.matches(matriculaRegex)) { return Err(VehiculoError.VehiculoNoValidoError("La matrícula no es válida")) }
    require(this.marca.isNotBlank()) { return Err(VehiculoError.VehiculoNoValidoError("El campo marca no puede estar vacío")) }
    require(this.modelo.isNotBlank()) { return Err(VehiculoError.VehiculoNoValidoError("El campo modelo no puede estar vacío")) }
    require(this.fechaMatriculacion <= LocalDate.now()) { return Err(VehiculoError.VehiculoNoValidoError("La fecha no puede ser superior a la actual")) }
    require(this.km >= 0) { return Err(VehiculoError.VehiculoNoValidoError("El kilometraje no puede ser negativo")) }
    return Ok(this)
}