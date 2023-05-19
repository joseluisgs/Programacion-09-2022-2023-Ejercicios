package dev.joseluisgs.expedientesacademicos.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.joseluisgs.expedientesacademicos.errors.CocheError
import dev.joseluisgs.expedientesacademicos.models.Coche
import java.time.LocalDate

fun Coche.validate(): Result<Coche, CocheError> {
    if (!this.matricula.matches("""^[0-9]{4}-[BCDFGHJKLMNPQRSTVWXYZ]{3}${'$'}""".toRegex())) {
        return Err(CocheError.ValidationProblem("La matrícula no cumple el formato correcto (sólo se admite NNNN-LLL)"))
    }
    if (this.marca.isEmpty()) {
        return Err(CocheError.ValidationProblem("La marca no puede estar vacía"))
    }
    if (this.modelo.isEmpty()) {
        return Err(CocheError.ValidationProblem("El modelo no puede estar vacío"))
    }
    if (this.fechaMatriculacion.isAfter(LocalDate.now())) {
        return Err(CocheError.ValidationProblem("La fecha de matriculación no puede ser posterior a la actual"))
    }
    return Ok(this)
}
