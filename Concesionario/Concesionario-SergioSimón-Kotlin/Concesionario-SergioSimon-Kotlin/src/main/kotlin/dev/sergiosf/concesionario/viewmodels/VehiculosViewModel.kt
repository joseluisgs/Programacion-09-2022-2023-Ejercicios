package dev.sergiosf.concesionario.viewmodels

import com.github.michaelbull.result.*
import dev.sergiosf.concesionario.errors.VehiculoError
import dev.sergiosf.concesionario.mappers.toModel
import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.repositories.VehiculoRepositoryImpl
import dev.sergiosf.concesionario.routes.RoutesManager
import dev.sergiosf.concesionario.service.storage.StorageVehiculosImp
import dev.sergiosf.concesionario.validators.validate
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}
private const val SIN_IMAGEN = "images/sin-imagen.jpg"

class VehiculosViewModel(
    private val repository: VehiculoRepositoryImpl,
    private val storage: StorageVehiculosImp
) {


    val state = SimpleObjectProperty(VehiculoState())

    init {
        logger.debug { "Inicializando VehículosViewModel" }
        loadVehiculosFromRepository()
        loadTypes()
    }

    private fun loadVehiculosFromRepository() {
        logger.debug { "Cargando vehículos del repositorio" }
        val lista = repository.findAll()
        logger.debug { "Cargando vehículos del repositorio: ${lista.size}" }
        updateState(lista)
    }

    private fun loadTypes() {
        logger.debug { "Cargando tipos de vehículos" }
        state.value = state.value.copy(
                typeVehiculo = listOf(
                        Vehiculo.TipoVehiculo.NONE.value,
                        Vehiculo.TipoVehiculo.GASOLINA.value,
                        Vehiculo.TipoVehiculo.DIESEL.value,
                        Vehiculo.TipoVehiculo.ELECTRICO.value,
                        Vehiculo.TipoVehiculo.HIBRIDO.value,
                )
        )
    }

    fun vechiculosFilteredList(tipoMotor: String, matricula: String): List<Vehiculo> {
        logger.debug { "Filtrando lista de Vehículos: $tipoMotor, $matricula" }

        return state.value.vehiculos
                .filter { vehiculo ->
                    when (tipoMotor) {
                        TipoFiltro.GASOLINA.value -> vehiculo.tipoVehiculo == Vehiculo.TipoVehiculo.GASOLINA
                        TipoFiltro.DIESEL.value -> vehiculo.tipoVehiculo == Vehiculo.TipoVehiculo.DIESEL
                        TipoFiltro.ELECTRICO.value -> vehiculo.tipoVehiculo == Vehiculo.TipoVehiculo.ELECTRICO
                        TipoFiltro.HIBRIDO.value -> vehiculo.tipoVehiculo == Vehiculo.TipoVehiculo.HIBRIDO
                        TipoFiltro.NONE.value -> vehiculo.tipoVehiculo == Vehiculo.TipoVehiculo.NONE
                        else -> true
                    }
                }.filter { vehiculo ->
                    vehiculo.marca.contains(matricula, true)
                }

    }

    enum class TipoFiltro(val value: String) {
        NONE("Ningun tipo"), GASOLINA("Gasolina"), DIESEL("Diesel"), ELECTRICO("Elelctrico"), HIBRIDO("Hibrido")
    }

    fun updateVehiculoSeleccionado(vehiculo: Vehiculo) {
        logger.debug { "Actualizando estado de vehículo: $vehiculo" }

        lateinit var fileImage: File
        lateinit var imagen: Image

        storage.loadImage(vehiculo.image).onSuccess {
            imagen = Image(it.absoluteFile.toURI().toString())
            fileImage = it
        }.onFailure {
            imagen = Image(RoutesManager.getResourceAsStream(SIN_IMAGEN))
            fileImage = File(RoutesManager.getResource(SIN_IMAGEN).toURI())
        }

        val vehiculoSeleccionado = VehiculoFormulario(
                id = vehiculo.id,
                marca = vehiculo.marca,
                modelo = vehiculo.modelo,
                matricula = vehiculo.matricula,
                tipoVehiculo = vehiculo.tipoVehiculo,
                fechaMatriculation = vehiculo.fechaMatriculation,
                imagen = imagen,
                fileImage = fileImage
        )

        state.value = state.value.copy(vehiculoSeleccionado = vehiculoSeleccionado)
    }

    fun crearVehiculo(vehiculoNuevo: VehiculoFormulario): Result<Vehiculo, VehiculoError> {
        logger.debug { "Creando vehiculo" }
        var newVehiculo = vehiculoNuevo.toModel().copy(id = Vehiculo.NEW_VEHICULO)
        return newVehiculo.validate(repository)
                .andThen {
                    println("Imagen a copiar: ${vehiculoNuevo.fileImage}")
                    vehiculoNuevo.fileImage?.let { newFileImage ->
                        storage.saveImage(newFileImage).onSuccess {
                            println("Imagen copiada: ${it.name}")
                            newVehiculo = newVehiculo.copy(image = it.name)
                        }
                    }
                    val new = repository.save(newVehiculo)
                    updateState(state.value.vehiculos + new)
                    Ok(new)
                }
    }

    private fun updateState(listaVehiculo: List<Vehiculo>) {
        logger.debug { "Actualizando estado de Aplicacion" }

        val vehiculoSeleccionado = VehiculoFormulario()

        state.value = state.value.copy(
                vehiculos = listaVehiculo.sortedBy { it.marca },
                vehiculoSeleccionado = vehiculoSeleccionado
        )
    }

    fun editarVehiculo(vehiculoEditado: VehiculoFormulario): Result<Vehiculo, VehiculoError> {
        logger.debug { "Editando Alumno" }
        val fileImageTemp = state.value.vehiculoSeleccionado.fileImage // Nombre de la imagen que tiene
        var updatedVehiculo = vehiculoEditado.toModel().copy(image = fileImageTemp!!.name)
        return updatedVehiculo.validate(repository)
                .andThen {
                    // Tenemos dos opciones, que no tuviese imagen o que si la tuviese
                    vehiculoEditado.fileImage?.let { newFileImage ->
                        if (updatedVehiculo.image == TipoImagen.SIN_IMAGEN.value || updatedVehiculo.image == TipoImagen.EMPTY.value) {
                            storage.saveImage(newFileImage).onSuccess {
                                updatedVehiculo = updatedVehiculo.copy(image = it.name)
                            }
                        } else {
                            storage.updateImage(fileImageTemp, newFileImage)
                        }
                    }
                    val updated = repository.save(updatedVehiculo)
                    updateState(
                            state.value.vehiculos.filter { it.id != updated.id } + updated
                    )
                    Ok(updated)
                }
    }

    fun eliminarVehiculo(): Result<Unit, VehiculoError> {
        logger.debug { "Eliminando Alumno" }
        val vehiculo = state.value.vehiculoSeleccionado.copy()

        vehiculo.fileImage?.let {
            if (it.name != TipoImagen.SIN_IMAGEN.value) {
                storage.deleteImage(it)
            }
        }

        repository.deleteById(vehiculo.id)
        updateState(state.value.vehiculos.filter { it.id != vehiculo.id })
        return Ok(Unit)
    }

    fun setTipoOperacion(tipo: TipoOperacion) {
        logger.debug { "Cambiando tipo de operación: $tipo" }
        state.value = state.value.copy(tipoOperacion = tipo)
    }

    fun getDefautltImage(): Image {
        return Image(RoutesManager.getResourceAsStream(SIN_IMAGEN))
    }

    enum class TipoOperacion(val value: String) {
        NUEVO("Nuevo"), EDITAR("Editar")
    }

    enum class TipoImagen(val value: String) {
        SIN_IMAGEN("sin-imagen.jpg"), EMPTY("")
    }

    data class VehiculoState(
            val typeVehiculo: List<String> = listOf(),
            val vehiculos: List<Vehiculo> = listOf(),

            val vehiculoSeleccionado: VehiculoFormulario = VehiculoFormulario(),
            val tipoOperacion: TipoOperacion = TipoOperacion.NUEVO // Tipo de operacion
    )

    data class VehiculoFormulario(
            val id: Long = Vehiculo.NEW_VEHICULO,
            val marca: String = "",
            val modelo: String = "",
            val matricula: String = "",
            val tipoVehiculo: Vehiculo.TipoVehiculo = Vehiculo.TipoVehiculo.NONE,
            val fechaMatriculation: LocalDate = LocalDate.now(),
            val imagen: Image = Image(RoutesManager.getResourceAsStream(SIN_IMAGEN)),
            var fileImage: File? = null
    )
}
