package es.sergiomisas.concesionario.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import es.sergiomisas.concesionario.errors.CocheError
import es.sergiomisas.concesionario.models.Coche
import java.time.LocalDate

fun Coche.validate(): Result<Coche, CocheError> {
    if (!this.matricula.matches(Regex("^[0-9]{4}[A-Z]{3}\$"))) {
        return Err(es.sergiomisas.concesionario.errors.CocheError.ValidationProblem("La matricula tiene que ser válida ej: 1234ABC"))
    }
    if (this.marca.isEmpty()) {
        return Err(es.sergiomisas.concesionario.errors.CocheError.ValidationProblem("La marca no puede estar vacía"))
    }
    if (this.modelo.isEmpty()) {
        return Err(es.sergiomisas.concesionario.errors.CocheError.ValidationProblem("El modelo no puede estar vacío"))
    }
    if (this.fechaMatriculacion.isAfter(LocalDate.now())) {
        return Err(es.sergiomisas.concesionario.errors.CocheError.ValidationProblem("La fecha de matriculación no puede ser posterior a hoy"))
    }
    return Ok(this)
}
