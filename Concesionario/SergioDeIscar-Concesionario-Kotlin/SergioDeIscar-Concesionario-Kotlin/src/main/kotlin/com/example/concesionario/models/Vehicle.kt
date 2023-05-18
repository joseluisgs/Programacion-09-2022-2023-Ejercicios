package com.example.concesionario.models

import java.time.LocalDate

data class Vehicle(
    val id: Long = -1L,
    val marca: String,
    val modelo: String,
    val motor: Motor,
    val fechaMatriculacion: LocalDate,
    val imagenUrl: String? = null
)
