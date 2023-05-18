package com.example.vehiculoscrudinterface.dto

import com.example.vehiculoscrudinterface.enums.TipoMotor
import java.time.LocalDate

class VehiculoDto(
    val id: String,
    val marca: String,
    val modelo: String,
    val tipoMotor: String,
    val km: String,
    val fechaMatriculacion: String,
    val imagen: String
) {
}