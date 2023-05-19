package dev.sbytmacke.vehiculositv.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sbytmacke.vehiculositv.errors.VehiculeError
import dev.sbytmacke.vehiculositv.models.Vehicule

class VehiculeValidator {
    fun validate(item: Vehicule): Result<Vehicule, VehiculeError> {
        return when {
            item.marca.isBlank() -> Err(VehiculeError.MarcaInvalid("La marca no puede estar vacia"))
            item.modelo.isBlank() -> Err(VehiculeError.ModeloInvalid("El modelo no puede estar vacio"))
            (item.typeMotor != Vehicule.TypeMotor.DIESEL
                    && item.typeMotor != Vehicule.TypeMotor.ELECTRICO
                    && item.typeMotor != Vehicule.TypeMotor.GASOLINA
                    && item.typeMotor != Vehicule.TypeMotor.HIBRIDO)
                    || item.typeMotor == Vehicule.TypeMotor.NONE -> Err(VehiculeError.TypeMotorInvalid("El tipo del motor es incorrecto"))

            item.km < 0 -> Err(VehiculeError.KmInvalid("El precio no puede ser menor de 0"))
            //item.fechaMantenimiento.isAfter(LocalDate.now()) -> Err(VehiculeError.FechaMantenimientoInvalid("La fecha no puede ser superior a la fecha de hoy"))
            else -> Ok(item)
        }
    }
}