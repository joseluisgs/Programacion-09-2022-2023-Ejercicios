package validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.jczhang.jczhangcocheskotlinjavafx.errors.CocheErrors
import com.jczhang.jczhangcocheskotlinjavafx.models.Coche
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import java.time.LocalDate

fun validate(
    marcaMostrar: TextField,
    modeloMostrar: TextField,
    kmMostrar: TextField,
    tipoMotorMostrar: TextField,
    matriculationMostrar: DatePicker
): Result<Coche, CocheErrors> {
    return when {
        marcaMostrar.text.isBlank() -> Err(CocheErrors.MarcaNoValida("La marca no puede estar en blanco"))
        modeloMostrar.text.isBlank() -> Err(CocheErrors.ModeloNoValido("El modelo no puede estar en blanco"))
        tipoMotorMostrar.text.uppercase() != "ELECTRICO" && tipoMotorMostrar.text.uppercase() != "HIBRIDO" && tipoMotorMostrar.text.uppercase() != "GASOLINA" && tipoMotorMostrar.text.uppercase() != "DIESEL" -> Err(
            CocheErrors.TipoNoValido("tipo invalido")
        )

        matriculationMostrar.value > LocalDate.now() -> Err(CocheErrors.FechaInvalida("La fecha de matriculacion no puede ser mayor a la actual"))
        kmMostrar.text.toDoubleOrNull() == null || kmMostrar.text.toDouble() < 0 -> Err(CocheErrors.KmNoValido("kilometros no validos"))

        else -> Ok(
            Coche(
                id = null,
                marca = marcaMostrar.text,
                modelo = modeloMostrar.text,
                km = kmMostrar.text.toDouble(),
                tipoMotor = tipoMotorMostrar.text,
                matriculacion = matriculationMostrar.value
            )
        )
    }
}

fun validate2(
    idMostrar: TextField,
    marcaMostrar: TextField,
    modeloMostrar: TextField,
    kmMostrar: TextField,
    tipoMotorMostrar: TextField,
    matriculationMostrar: DatePicker
): Result<Coche, CocheErrors> {
    return when {
        marcaMostrar.text.isBlank() -> Err(CocheErrors.MarcaNoValida("La marca no puede estar en blanco"))
        modeloMostrar.text.isBlank() -> Err(CocheErrors.ModeloNoValido("El modelo no puede estar en blanco"))
        tipoMotorMostrar.text.uppercase() != "ELECTRICO" && tipoMotorMostrar.text.uppercase() != "HIBRIDO" && tipoMotorMostrar.text.uppercase() != "GASOLINA" && tipoMotorMostrar.text.uppercase() != "DIESEL" -> Err(
            CocheErrors.TipoNoValido("tipo invalido")
        )

        matriculationMostrar.value > LocalDate.now() -> Err(CocheErrors.FechaInvalida("La fecha de matriculacion no puede ser mayor a la actual"))
        kmMostrar.text.toDoubleOrNull() == null || kmMostrar.text.toDouble() < 0 -> Err(CocheErrors.KmNoValido("kilometros no validos"))

        else -> Ok(
            Coche(
                id = idMostrar.text.toLong(),
                marca = marcaMostrar.text,
                modelo = modeloMostrar.text,
                km = kmMostrar.text.toDouble(),
                tipoMotor = tipoMotorMostrar.text,
                matriculacion = matriculationMostrar.value
            )
        )
    }
}

