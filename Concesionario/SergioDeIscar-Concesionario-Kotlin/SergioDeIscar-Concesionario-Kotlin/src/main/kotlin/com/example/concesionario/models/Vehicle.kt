package com.example.concesionario.models

import java.time.LocalDate

data class Vehicle(
    val id: Long = -1L,
    val marca: String,
    val modelo: String,
    val motor: Motor,
    val fehcaMatriculacion: LocalDate,
    val imagenUrl: String? = null
)
