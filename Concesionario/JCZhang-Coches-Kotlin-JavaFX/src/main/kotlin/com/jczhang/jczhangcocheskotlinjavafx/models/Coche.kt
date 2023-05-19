package com.jczhang.jczhangcocheskotlinjavafx.models


import java.time.LocalDate

data class Coche(
    val id: Long?,
    val marca: String,
    val modelo: String,
    val tipoMotor: String,
    val km: Double,
    val matriculacion: LocalDate
)
