package dev.sbytmacke.vehiculositv.mappers

import database.VehiculeTable
import dev.sbytmacke.vehiculositv.models.Vehicule
import dev.sbytmacke.vehiculositv.states.VehiculeDetailsState
import java.util.*

fun toVehiculeOfSQDelight(itemTable: VehiculeTable): Vehicule {
    return Vehicule(
        itemTable.id,
        itemTable.matricule,
        itemTable.marca,
        itemTable.modelo,
        toTypeMotor(itemTable.tipoMotor),
        itemTable.km,
        itemTable.fechaMantenimiento
    )
}

fun toVehiculeOfDetails(vehiculeDetails: VehiculeDetailsState): Vehicule {
    return Vehicule(
        vehiculeDetails.id,
        vehiculeDetails.matricule,
        vehiculeDetails.marca,
        vehiculeDetails.modelo,
        vehiculeDetails.typeMotor,
        vehiculeDetails.km,
        vehiculeDetails.fechaMantenimiento.toString()
    )
}

fun toTypeMotor(typeString: String): Vehicule.TypeMotor {
    return when (typeString.uppercase(Locale.getDefault())) {
        "ELECTRICO" -> Vehicule.TypeMotor.ELECTRICO
        "HIBRIDO" -> Vehicule.TypeMotor.HIBRIDO
        "DIESEL" -> Vehicule.TypeMotor.DIESEL
        "GASOLINA" -> Vehicule.TypeMotor.GASOLINA
        else -> Vehicule.TypeMotor.NONE
    }
}