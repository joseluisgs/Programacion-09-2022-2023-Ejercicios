package com.example.concesionario.states

import com.example.concesionario.models.Vehicle

data class VehicleState(
    val vehicles: List<Vehicle> = listOf(),
    val motores: List<String> = listOf(),
    val id: String = "",
    val marca: String = "",
    val modelo: String = "",
    val motor: String = "",
    val fechaMatriculacion: String = "",
    val imagenUrl: String = "images/default.png",
    val actionType: ActionType = ActionType.NEW
)

enum class ActionType {
    NEW,
    EDIT
}