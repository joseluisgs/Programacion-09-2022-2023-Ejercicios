package com.example.concesionario.states

data class FormState(
    val motores: List<String> = listOf(),
    val id: String = "0",
    val marca: String = "",
    val modelo: String = "",
    val motor: String = "",
    val fechaMatriculacion: String = "",
    val imagenUrl: String = "",
)
