package dev.sbytmacke.vehiculositv.viewmodels

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sbytmacke.vehiculositv.errors.VehiculeError
import dev.sbytmacke.vehiculositv.mappers.toVehiculeOfDetails
import dev.sbytmacke.vehiculositv.models.Vehicule
import dev.sbytmacke.vehiculositv.repositories.VehiculesRepository
import dev.sbytmacke.vehiculositv.services.storages.VehiculesStorage
import dev.sbytmacke.vehiculositv.states.DataState
import dev.sbytmacke.vehiculositv.states.VehiculeDetailsState
import dev.sbytmacke.vehiculositv.validators.VehiculeValidator
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class MainViewModel(
    private val repository: VehiculesRepository,
    private val storage: VehiculesStorage,
) {

    // Datos del estado que serán compartidos entre el ViewModel y el controlador interfaz
    val stateVehicule = SimpleObjectProperty(DataState())

    init {
        logger.debug { "Actualizando estado de Aplicacion" }

        // Tipos de vehçículos para en el futuro buscar por el tipo de vehículo
        val typesMotor: List<String> = listOf(
            Vehicule.TypeMotor.ALL.value,
            Vehicule.TypeMotor.HIBRIDO.toString(),
            Vehicule.TypeMotor.ELECTRICO.toString(),
            Vehicule.TypeMotor.GASOLINA.toString(),
            Vehicule.TypeMotor.DIESEL.toString()
        )

        // Para favorecer la inmutabilidad copiamos la clase con los valores deseados
        stateVehicule.value = stateVehicule.value.copy(
            typesVehicules = typesMotor,
            vehicules = repository.findAll().sortedBy { it.id }, // Recogemos de la base de datos todos los vehículos
            vehiculeSeleccionado = VehiculeDetailsState() // Por defecto uno estándar, después lo actualizaremos en updateItemSelected(), cuando queramos manipularlo
        )
    }

    fun createVehicule(newVehicule: VehiculeDetailsState): Result<Vehicule?, VehiculeError> {
        logger.debug { "Creando Vehiculo" }

        val newVehiculeCopy = toVehiculeOfDetails(newVehicule).copy(Vehicule.NEW_VEHICULE)

        val validationResult = VehiculeValidator().validate(newVehiculeCopy)

        if (validationResult is Err) {
            return Err(validationResult.error)
        }

        repository.save(newVehiculeCopy)
        val getVehiculeFreeConcurrence: Vehicule? = repository.getByMatricule(newVehiculeCopy.matricule)
        updateState(stateVehicule.value.vehicules + getVehiculeFreeConcurrence)

        return Ok(getVehiculeFreeConcurrence)
    }

    fun updateVehicule(updatedVehicule: VehiculeDetailsState): Result<Vehicule, VehiculeError> {
        logger.debug { "Editando Vehiculo" }

        val updatedVehiculeCopy = toVehiculeOfDetails(updatedVehicule).copy()

        val validationResult = VehiculeValidator().validate(updatedVehiculeCopy)

        if (validationResult is Err) {
            return Err(validationResult.error)
        }

        repository.save(updatedVehiculeCopy)
        val getVehiculeFreeConcurrence: Vehicule? = repository.getByMatricule(updatedVehiculeCopy.matricule)
        updateState(stateVehicule.value.vehicules.filter { it!!.id != getVehiculeFreeConcurrence!!.id } + getVehiculeFreeConcurrence)

        return Ok(updatedVehiculeCopy)
    }

    fun eliminarAlumno(): Boolean {
        logger.debug { "Eliminando Vehiculo" }

        val vehiculeToDelete = stateVehicule.value.vehiculeSeleccionado.copy()

        // Borramos del repositorio
        repository.deleteById(vehiculeToDelete.id)

        // Actualizamos el estado
        updateState(stateVehicule.value.vehicules.filter { it!!.id != vehiculeToDelete.id })

        return true
    }

    fun vehiculoFilteredList(tipo: Vehicule.TypeMotor?, matricula: String?, fecha: LocalDate?): List<Vehicule?> {
        logger.debug { "Filtrando lista en función de: $tipo, con la matrícula $matricula y fecha $fecha" }

        return stateVehicule.value.vehicules.filter { vehicule ->
            // Filtramos por el tipo del vehículo/motor
            when (tipo) {
                Vehicule.TypeMotor.DIESEL -> vehicule!!.typeMotor == Vehicule.TypeMotor.DIESEL
                Vehicule.TypeMotor.GASOLINA -> vehicule!!.typeMotor == Vehicule.TypeMotor.GASOLINA
                Vehicule.TypeMotor.ELECTRICO -> vehicule!!.typeMotor == Vehicule.TypeMotor.ELECTRICO
                Vehicule.TypeMotor.HIBRIDO -> vehicule!!.typeMotor == Vehicule.TypeMotor.HIBRIDO

                // Cualquier otro valor del filtro nos dará todos los items
                else -> true
            }
        }.filter { vehicule ->
            // En caso de darnos una matrícula filtramos por esa matrícula
            if (matricula != null) {
                vehicule!!.matricule.contains(matricula)
            } else {
                // Si es nula nos devuelve
                true
            }
        }.filter { vehicule ->
            // En caso de darnos una fecha filtramos por esa fecha
            if (fecha != null) {
                vehicule!!.fechaMantenimiento == fecha.toString()
            } else {
                // Si es nula nos devuelve
                true
            }
        }
    }

    /**
     * Actualiza el estado de la aplicación general, es decir toda la lista de los items que
     * tengamos u otros aspectos que puedan actualizar el estado de la interfaz.
     */
    private fun updateState(listVehicules: List<Vehicule?>) {
        logger.debug { "Actualizando estado de Aplicacion" }

        // Para favorecer la inmutabilidad copiamos la clase con los valores deseados
        stateVehicule.value = stateVehicule.value.copy(
            vehicules = listVehicules.sortedBy { it!!.id }, // Recogemos los nuevos vehículos
            vehiculeSeleccionado = VehiculeDetailsState() // Por defecto uno estándar, después lo actualizaremos en updateItemSelected(), cuando queramos manipularlo
        )
    }

    fun updateVehiculeSelected(vehicule: Vehicule) {
        logger.debug { "Actualizando estado de: ${vehicule.matricule}" }

        val vehiculeSelected = VehiculeDetailsState(
            vehicule.id,
            vehicule.matricule,
            vehicule.marca,
            vehicule.modelo,
            vehicule.typeMotor,
            vehicule.km,
            vehicule.fechaMantenimiento
        )

        stateVehicule.value = stateVehicule.value.copy(vehiculeSeleccionado = vehiculeSelected)
    }

    fun exportToJson(file: File): Result<Long, VehiculeError> {
        logger.debug { "Guardando Alumnado en JSON" }
        return storage.exportToJson(file, stateVehicule.value.vehicules)
    }
}