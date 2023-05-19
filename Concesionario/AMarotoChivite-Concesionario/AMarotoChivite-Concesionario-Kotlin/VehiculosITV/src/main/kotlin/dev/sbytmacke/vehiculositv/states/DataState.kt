package dev.sbytmacke.vehiculositv.states

import dev.sbytmacke.vehiculositv.enums.TypeOperation
import dev.sbytmacke.vehiculositv.models.Vehicule

// Estado del ViewModel y caso de uso de Gestión de Vehiculos
data class DataState(
    // Los contenedores de colecciones deben ser ObservableList
    val typesVehicules: List<String> = listOf(),
    val vehicules: List<Vehicule?> = listOf(),

    // Vehículo seleccionado en tabla de la interfaz para poderlo pasar a DETAILS
    val vehiculeSeleccionado: VehiculeDetailsState = VehiculeDetailsState(),

    // Tipo de operacion, por defecto NUEVA, si queremos editar debemos decidirlo en el controller
    val tipoOperacion: TypeOperation = TypeOperation.NUEVO,
)