package com.example.gestionvehiculosconimagenes_kotlin.viewmodel

import com.example.gestionvehiculosconimagenes_kotlin.error.VehiculoError
import com.example.gestionvehiculosconimagenes_kotlin.mapper.toVehiculo
import com.example.gestionvehiculosconimagenes_kotlin.mapper.toVehiculoReference
import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.IVehiculoRepository
import com.example.gestionvehiculosconimagenes_kotlin.service.storage.IStorageImages
import com.example.gestionvehiculosconimagenes_kotlin.utils.getResource
import com.example.gestionvehiculosconimagenes_kotlin.utils.getResourceAsStream
import com.example.gestionvehiculosconimagenes_kotlin.validator.validate
import com.github.michaelbull.result.*
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

class ViewModel(
    private val repository: IVehiculoRepository,
    private val storageFotos: IStorageImages
) {

    val state = SimpleObjectProperty(SharedState())

    init{
        loadTipoMotores()

        state.value = state.value.copy(
            vehiculos = repository.getAll()
        )
    }

    private fun loadTipoMotores() {
        logger.debug { "Cargamos los tipos de motores que hay" }
        state.value = state.value.copy(
            comboBoxOptions = TipoMotor.values().map { it.toString() }
        )
    }

    fun saveVehiculo(vehiculo: VehiculoReference): Result<Vehiculo, VehiculoError>{
        logger.debug { "Se almacena un vehiculo" }

        var vehiculoC = vehiculo.toVehiculo().copy(id = Vehiculo.VEHICULO_ID)

        return vehiculoC.validate().andThen {

            vehiculo.fotoFile?.let { newFileImage ->
                storageFotos.saveImage(newFileImage).onSuccess {
                    println("Imagen copiada: ${it.name}")
                    vehiculoC = vehiculoC.copy(foto = it.name)
                }
            }

            vehiculoC = repository.save(vehiculoC)
            updateSharedState(state.value.vehiculos + vehiculoC)
            Ok(vehiculoC)
        }
    }

    fun updateVehiculo(vehiculo: VehiculoReference): Result<Vehiculo, VehiculoError>{
        logger.debug { "Se almacena un vehiculo" }

        val oldImage = state.value.vehiculoReference.fotoFile
        var vehiculoC = vehiculo.toVehiculo().copy(foto = oldImage!!.name)

        return vehiculoC.validate().andThen {
            if(vehiculoC == state.value.vehiculoReference.toVehiculo()){
                return Err(VehiculoError.SameDateUpdate(vehiculoC.id))
            }

            vehiculo.fotoFile?.let { newFileImage ->
                if (vehiculoC.foto == vehiculoC.tipoMotor.imagePath.removePrefix("images/")) {
                    storageFotos.saveImage(newFileImage).onSuccess {
                        vehiculoC = vehiculoC.copy(foto = it.name)
                    }
                } else {
                    storageFotos.updateImage(oldImage, newFileImage)
                }
            }

            vehiculoC = repository.save(vehiculoC)

            updateSharedState(state.value.vehiculos.filter { it.id != vehiculoC.id } + vehiculoC)
            Ok(vehiculoC)
        }
    }

    fun onSelectedUpdateVehiculoReference(vehiculo: Vehiculo) {
        logger.debug { "Se actualizan los datos del vehiculo a mostar según la selección en la tabla" }

        lateinit var fileImage: File
        lateinit var image: Image

        storageFotos.loadImage(vehiculo.foto).onSuccess {
            image = Image(it.absoluteFile.toURI().toString())
            fileImage = it
        }.onFailure {
            //En caso de que algo falle al cargar la imagen, se le asignará una imagén por defecto
            image = Image(getResourceAsStream(vehiculo.tipoMotor.imagePath))
            fileImage = File(getResource(vehiculo.tipoMotor.imagePath).toURI())
        }

        state.value = state.value.copy(
            vehiculoReference = vehiculo.toVehiculoReference(image, fileImage)
        )
    }

    fun filterTable(tipoMotor: TipoMotor, marcaModelo: String): List<Vehiculo> {
        logger.debug { "Filtramos la tabla según el tipo de motor: $tipoMotor y la marca modelo: $marcaModelo" }
        return state.value.vehiculos
            .filter {
                it.marcaModelo.lowercase().contains(marcaModelo.lowercase())
            }
            .filter {
                if (tipoMotor == TipoMotor.CUALQUIERA) {
                    true
                } else {
                    it.tipoMotor == tipoMotor
                }
            }
    }

    fun deleteVehiculo(): Result<Unit, VehiculoError> {
        logger.debug { "Intentamos eliminar el vehículo actualmente seleccionado" }

        val vehiculo = state.value.vehiculoReference

        vehiculo.fotoFile?.let {
            storageFotos.deteleImage(it)
        }

        repository.delete(vehiculo.id)

        updateSharedState(state.value.vehiculos.filter { it.id != vehiculo.id })
        return Ok(Unit)
    }

    private fun updateSharedState(vehiculos: List<Vehiculo>) {
        logger.debug { "Actualizamos el estado compartido tras la operación que modificó la base de datos" }
        state.value = state.value.copy(
            vehiculoReference = VehiculoReference(),
            vehiculos = vehiculos
        )
    }

    fun setTipoOperacion(operacion: TipoOperacion) {
        logger.debug { "Cambiamos el tipo de la operación" }
        if(operacion != state.value.tipoOperacion){
            state.value = state.value.copy(
                tipoOperacion = operacion
            )
        }
    }
}

data class SharedState(
    val vehiculoReference: VehiculoReference = VehiculoReference(),
    val tipoOperacion: TipoOperacion = TipoOperacion.AÑADIR,
    val vehiculos: List<Vehiculo> = listOf(),
    val comboBoxOptions: List<String> = listOf(),
)

enum class TipoOperacion {
    AÑADIR, EDITAR
    // No le pongo un tipo BORRAR, ya que no será necesario para definir despues la vista de la ventana modal, porque el metodo borrar no tendrá ventana modal.
}

data class VehiculoReference(
    val id: Long = Vehiculo.VEHICULO_ID,
    val matricula: String = "",
    val marca: String = "",
    val modelo: String = "",
    val tipoMotor: TipoMotor? = null,
    val km: Double = 0.0,
    val fechaMatriculacion: LocalDate? = null,
    val foto: Image = Image(getResourceAsStream("images/not_found.png")),
    val fotoFile: File? = null
)