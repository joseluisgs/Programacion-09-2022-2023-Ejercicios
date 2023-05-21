package dev.sergiosf.concesionario.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.concesionario.errors.VehiculoError
import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.repositories.VehiculoRepositoryImpl
import java.time.LocalDate


fun Vehiculo.validate(repository: VehiculoRepositoryImpl): Result<Vehiculo, VehiculoError> {

    val validateMatricula = repository.findByMatricula(this.matricula)?.id
    val matriculaRegex = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}".toRegex()
    val stringRegex = "[a-zA-Z]+\\d*".toRegex()

    if (this.marca.isEmpty()) {
        return Err(VehiculoError.ValidationProblem("La marca no puede estar vacía"))
    }
    if (!stringRegex.matches(this.marca)){
        return Err(VehiculoError.ValidationProblem("Marca invalida, no esta bien formada"))
    }
    if (this.modelo.isEmpty()) {
        return Err(VehiculoError.ValidationProblem("El modelo no puede estar vacío"))
    }
    if (!stringRegex.matches(this.modelo)){
        return Err(VehiculoError.ValidationProblem("Modelo invalida, no esta bien formada"))
    }
    if (this.matricula.isEmpty()) {
        return Err(VehiculoError.ValidationProblem("El modelo no puede estar vacío"))
    }
    if (!matriculaRegex.matches(this.matricula)){
        return Err(VehiculoError.ValidationProblem("Matricula invalida, no esta bien formada"))
    }
    if (validateMatricula != null && validateMatricula != this.id) {
        return Err(VehiculoError.ValidationProblem("Matricula ya registrada"))
    }
    if (this.fechaMatriculation.isAfter(LocalDate.now())) {
        return Err(VehiculoError.ValidationProblem("La fecha de matriculación no puede ser posterior a hoy"))
    }
    return Ok(this)
}