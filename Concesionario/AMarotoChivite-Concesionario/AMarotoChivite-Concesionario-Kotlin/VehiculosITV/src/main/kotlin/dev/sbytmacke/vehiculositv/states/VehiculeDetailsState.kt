package dev.sbytmacke.vehiculositv.states

import dev.sbytmacke.vehiculositv.models.Vehicule

// Estado para detalles (seleccionado y de operaciones)
data class VehiculeDetailsState(
    val id: Long = Vehicule.NEW_VEHICULE,
    val matricule: String = "",
    val marca: String = "",
    val modelo: String = "",
    val typeMotor: Vehicule.TypeMotor = Vehicule.TypeMotor.NONE,
    val km: Double = 0.0,
    val fechaMantenimiento: String = "",//LocalDate.now()
)