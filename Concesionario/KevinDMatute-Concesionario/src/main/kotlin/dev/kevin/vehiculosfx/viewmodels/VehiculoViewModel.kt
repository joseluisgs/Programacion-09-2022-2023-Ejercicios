package dev.kevin.vehiculosfx.viewmodels

import com.github.michaelbull.result.*
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.kevin.vehiculosfx.enums.tipoMotor
import dev.kevin.vehiculosfx.errors.VehiculoError
import dev.kevin.vehiculosfx.mappers.toModel
import dev.kevin.vehiculosfx.models.Vehiculo
import dev.kevin.vehiculosfx.repositories.VehiculoRepository
import dev.kevin.vehiculosfx.routes.RoutesManager
import dev.kevin.vehiculosfx.services.storage.StorageVehiculos
import dev.kevin.vehiculosfx.validator.validate
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

val logger = KotlinLogging.logger {  }

private const val SIN_IMAGEN = "images/sin-imagen.png"

class VehiculoViewModel(
    private val repository: VehiculoRepository,
    private val storage: StorageVehiculos
) {
    val state = SimpleObjectProperty(ExpedienteState())

    init {
        logger.debug { "Iniciando VehiculoViewModel" }
        loadVehiculosFromRespository()
        loadTipos()
    }

    private fun loadVehiculosFromRespository() {
        logger.debug { "Cargando vehiculos del repositorio" }
        val lista = repository.findAll()
        updateState(lista)
    }

    private fun loadTipos() {
        state.value = state.value.copy(
            tiposMotor = listOf(
                TipoFiltro.DIESEL.value,
                TipoFiltro.ELECTRICO.value,
                TipoFiltro.GASOLINA.value,
                TipoFiltro.HIBRIDO.value
            )
        )
    }

    private fun updateState(lista: List<Vehiculo>) {
        logger.debug { "Se actualiza el estado de la aplicacion" }
        val vehiculoSeleccionado = VehiculoState()

        state.value = state.value.copy(
            vehiculos = lista.sortedBy { it.id },
            vehiculoSeleccionado = vehiculoSeleccionado
        )
    }

    fun vehiculosFilteredList(tipo: String, nombre: String): List<Vehiculo> {
        return state.value.vehiculos
            .filter { vehiculo ->
                when (tipo) {
                    TipoFiltro.HIBRIDO.value -> vehiculo.motor == tipoMotor.HIBRIDO.value
                    TipoFiltro.DIESEL.value -> vehiculo.motor == tipoMotor.DIESEL.value
                    TipoFiltro.GASOLINA.value -> vehiculo.motor == tipoMotor.GASOLINA.value
                    TipoFiltro.ELECTRICO.value -> vehiculo.motor == tipoMotor.ELECTRICO.value
                    else -> true
                }
            }.filter{
                vehiculo ->
                vehiculo.modelo.contains(nombre, true)
            }
    }

    fun updateVehiculoSeleccionado(vehiculo: Vehiculo) {
        lateinit var imagen: Image
        lateinit var fileImage: File

        storage.loadImage(vehiculo.imagen).onSuccess {
            imagen = Image(it.absoluteFile.toURI().toString())
            fileImage = it
        }.onFailure {
            imagen = Image(RoutesManager.gerResourcesAsStream(SIN_IMAGEN))
            fileImage = File(RoutesManager.getResource(SIN_IMAGEN).toURI())
        }

        val vehiculoSeleccionado = VehiculoState(
            id = vehiculo.id,
            marca = vehiculo.marca,
            matricula = vehiculo.matricula,
            modelo = vehiculo.modelo,
            motor = vehiculo.motor,
            km = vehiculo.km,
            fechaMatriculacion = vehiculo.fechaMatriculacion,
            imagen = imagen
        )
        state.value = state.value.copy(vehiculoSeleccionado = vehiculoSeleccionado)
    }

    fun crearVehiculo(nuevoVehiculo: VehiculoState): Result<Vehiculo, VehiculoError> {
        println("Nuevo vehiculo: $nuevoVehiculo")
        var newVehiculo = Vehiculo(
            id = Vehiculo.NEW_VEHICULO,
            marca = nuevoVehiculo.toModel().marca,
            matricula = nuevoVehiculo.toModel().matricula,
            modelo = nuevoVehiculo.toModel().modelo,
            motor = nuevoVehiculo.toModel().motor,
            km = nuevoVehiculo.toModel().km,
            fechaMatriculacion = nuevoVehiculo.toModel().fechaMatriculacion,
            imagen = nuevoVehiculo.toModel().imagen
        )
        return newVehiculo.validate()
            .andThen {
                nuevoVehiculo.fileImagen?.let { newFileImage ->
                    storage.saveImage(newFileImage).onSuccess {
                        println("Imagen copiada")
                        newVehiculo = Vehiculo(
                            id = Vehiculo.NEW_VEHICULO,
                            marca = nuevoVehiculo.marca,
                            matricula = nuevoVehiculo.matricula,
                            modelo = nuevoVehiculo.modelo,
                            motor = nuevoVehiculo.motor,
                            km = nuevoVehiculo.km,
                            fechaMatriculacion = nuevoVehiculo.fechaMatriculacion,
                            imagen = it.name
                        )
                    }
                }
                val new = repository.save(newVehiculo)
                updateState(state.value.vehiculos + new)
                Ok(new)
            }
    }

    fun editarVehiculo(vehiculoEditado: VehiculoState): Result<Vehiculo, VehiculoError> {
        logger.debug { "Editando vehiculo" }
        val fileImageTemp = state.value.vehiculoSeleccionado.fileImagen
        var updatedVehiculo = Vehiculo(
            id = Vehiculo.NEW_VEHICULO,
            marca = vehiculoEditado.marca,
            matricula = vehiculoEditado.matricula,
            modelo = vehiculoEditado.modelo,
            motor = vehiculoEditado.motor,
            km = vehiculoEditado.km,
            fechaMatriculacion = vehiculoEditado.fechaMatriculacion,
            imagen = vehiculoEditado.imagen.toString()
        )
        return updatedVehiculo.validate().andThen {
            vehiculoEditado.fileImagen?.let { newFileImage ->
                if (updatedVehiculo.imagen == TipoImagen.SIN_IMAGEN.value || updatedVehiculo.imagen == TipoImagen.EMPTY.value) {
                    storage.saveImage(newFileImage).onSuccess {
                        updatedVehiculo = Vehiculo(
                            id = Vehiculo.NEW_VEHICULO,
                            marca = updatedVehiculo.marca,
                            matricula = updatedVehiculo.matricula,
                            modelo = updatedVehiculo.modelo,
                            motor = updatedVehiculo.motor,
                            km = updatedVehiculo.km,
                            fechaMatriculacion = updatedVehiculo.fechaMatriculacion,
                            imagen = vehiculoEditado.imagen.toString()
                        )
                    }
                } else {
                    if (fileImageTemp != null) {
                        storage.updateImage(fileImageTemp, newFileImage)
                    } else {

                    }
                }
            }
            val updated = repository.save(updatedVehiculo)
            updateState(
                state.value.vehiculos.filter { it.id != updated.id } + updated
            )
            Ok(updated)
        }
    }

    fun eliminarVehiculo(): Result<Unit, VehiculoError>{
        logger.debug { "Eliminando Vehiculo" }
        val vehiculo = state.value.vehiculoSeleccionado.copy()

        vehiculo.fileImagen?.let {
            if(it.name != TipoImagen.SIN_IMAGEN.value){
                storage.deleteImage(it)
            }
        }
        repository.deleteById(vehiculo.id)
        updateState(state.value.vehiculos.filter { it.id != vehiculo.id})
        return Ok(Unit)
    }

    fun setTipoOperacion(tipo: TipoOperacion){
        logger.debug { "Cambiandod el tipo de la operacion" }
        state.value = state.value.copy(vehiculoOperacion = tipo)
    }

    fun getDefaultImage(): Image{
        return Image(RoutesManager.gerResourcesAsStream(SIN_IMAGEN))
    }

    enum class TipoImagen(val value: String) {
        SIN_IMAGEN("sin-imagen.png"), EMPTY("")
    }

    enum class TipoOperacion(val value: String) {
        NUEVO("Nuevo"), EDITAR("Editar")
    }

    enum class TipoFiltro(val value: String) {
        HIBRIDO("Hibrido"), ELECTRICO("Electrico"), GASOLINA("Gasolina"), DIESEL("Diesel")
    }

    data class ExpedienteState(
        val tiposMotor: List<String> = listOf(),
        val vehiculos: List<Vehiculo> = listOf(),

        val vehiculoSeleccionado: VehiculoState = VehiculoState(),
        val vehiculoOperacion: TipoOperacion = TipoOperacion.NUEVO
    )

    data class VehiculoState(
        val id: Long = Vehiculo.NEW_VEHICULO,
        val marca: String = "",
        val matricula: String = "",
        val modelo: String = "",
        val motor: String = "",
        val km: Int = 0,
        val fechaMatriculacion: LocalDate = LocalDate.now(),
        val imagen: Image = Image(RoutesManager.gerResourcesAsStream(SIN_IMAGEN)),
        var fileImagen: File? = null
    )
}
