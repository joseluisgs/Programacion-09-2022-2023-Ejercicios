package com.example.vehiculoscrudinterface.models

import com.example.vehiculoscrudinterface.enums.TipoMotor
import javafx.scene.image.Image
import java.time.LocalDate
import java.util.Date

data class Vehiculo(
    val id: String,
    var marca: String,
    var modelo: String,
    var tipoMotor: TipoMotor,
    var km: Int,
    var fechaMatriculacion: LocalDate,
    var imagen: String
    ) {
    override fun toString(): String {
        return "Vehiculo - " +
                "Matrícula: $id"
    }
}