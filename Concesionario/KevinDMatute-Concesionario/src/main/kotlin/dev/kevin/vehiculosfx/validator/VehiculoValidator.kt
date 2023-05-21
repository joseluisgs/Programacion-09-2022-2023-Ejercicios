package dev.kevin.vehiculosfx.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kevin.vehiculosfx.errors.VehiculoError
import dev.kevin.vehiculosfx.models.Vehiculo
import java.time.LocalDate

fun Vehiculo.validate(): Result<Vehiculo, VehiculoError>{
    if(this.marca.isEmpty()){
        return Err(VehiculoError.ValidationError("La marca no puede estar vacía"))
    }
    if(this.modelo.isEmpty()){
        return Err(VehiculoError.ValidationError("El modelo no puede estar vacía"))
    }
    if(this.km < 0){
        return Err(VehiculoError.ValidationError("Los kilometros no pueden ser mes de 0"))
    }
    if(this.fechaMatriculacion.isAfter(LocalDate.now())){
        return Err(VehiculoError.ValidationError("La fecha no puede ser mayor que la actual"))
    }
    return Ok(this)
}