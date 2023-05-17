package com.example.concesionario.dto

data class VehicleDto(
    val id: String = "0",
    val marca: String,
    val modelo: String,
    val motor: String,
    val fehcaMatriculacion: String,
    val imagenUrl: String? = null
)