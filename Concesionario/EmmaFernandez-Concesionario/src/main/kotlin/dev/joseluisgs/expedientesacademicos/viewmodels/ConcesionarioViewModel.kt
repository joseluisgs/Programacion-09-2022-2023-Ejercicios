package dev.joseluisgs.expedientesacademicos.viewmodels

import com.github.michaelbull.result.*
import dev.joseluisgs.expedientesacademicos.errors.CocheError
import dev.joseluisgs.expedientesacademicos.mappers.toModel
import dev.joseluisgs.expedientesacademicos.models.Coche
import dev.joseluisgs.expedientesacademicos.repositories.CochesRepository
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import dev.joseluisgs.expedientesacademicos.services.storage.StorageCoches
import dev.joseluisgs.expedientesacademicos.validators.validate
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

private const val SIN_IMAGEN = "images/sin-imagen.png"

class ConcesionarioViewModel(
    private val repository: CochesRepository,
    private val storage: StorageCoches
) {

    // Estado del ViewModel
    val state = SimpleObjectProperty(ConcesionarioState())

    init {
        logger.debug { "Inicializando ExpedientesViewModel" }
        loadCochesFromRepository() // Cargamos los datos del repositorio
        loadTypes() // Cargamos los tipos de repetidor
    }

    private fun loadTypes() {
        logger.debug { "Cargando tipos de repetidor" }
        state.value = state.value.copy(
            typesMotor = listOf(
                TipoFiltro.TODOS.value,
                TipoFiltro.GASOLINA.value,
                TipoFiltro.DIESEL.value,
                TipoFiltro.HIBRIDO.value,
                TipoFiltro.ELECTRICO.value
            )
        )
    }

    private fun loadCochesFromRepository() {
        logger.debug { "Cargando coches del repositorio" }
        val lista = repository.findAll()
        logger.debug { "Cargando coches del repositorio: ${lista.size}" }
        updateState(lista)
    }

    // Actualiza el estado de la aplicación con los datos de ese instante en el estado
    private fun updateState(listaCoches: List<Coche>) {
        logger.debug { "Actualizando estado de Aplicacion" }
        val cocheSeleccionado = CocheFormulario()

        state.value = state.value.copy(
            coches = listaCoches.sortedBy { it.marca },
            cocheSeleccionado = cocheSeleccionado
        )
    }

    // Filtra la lista de coches en el estado en función del tipo
    fun cochesFilteredList(tipo: String, matricula: String): List<Coche> {
        logger.debug { "Filtrando lista de Coches: $tipo" }

        return state.value.coches
            .filter { coche ->
                when (tipo) {
                    TipoFiltro.GASOLINA.value -> coche.tipoMotor == Coche.TipoMotor.GASOLINA
                    TipoFiltro.DIESEL.value -> coche.tipoMotor == Coche.TipoMotor.DIESEL
                    TipoFiltro.HIBRIDO.value -> coche.tipoMotor == Coche.TipoMotor.HIBRIDO
                    TipoFiltro.ELECTRICO.value -> coche.tipoMotor == Coche.TipoMotor.ELECTRICO
                    else -> true
                }
            }.filter { coche ->
                coche.matricula.contains(matricula)
            }
    }

    fun saveAlumnadoToJson(file: File): Result<Long, CocheError> {
        logger.debug { "Guardando Coches en JSON" }
        return storage.storeDataJson(file, state.value.coches)
    }

    fun loadAlumnadoFromJson(file: File, withImages: Boolean = false): Result<List<Coche>, CocheError> {
        logger.debug { "Cargando Coches de JSON" }
        // Borramos todas las imagenes e iniciamos el proceso
        return storage.deleteAllImages().andThen {
            storage.loadDataJson(file).onSuccess {
                repository.deleteAll() // Borramos todos los datos de la BD
                // Guardamos los nuevos, pero hay que quitarle el ID, porque trabajamos con el NEW!!
                repository.saveAll(
                    if (withImages)
                        it
                    else
                        it.map { a -> a.copy(id = Coche.NEW_COCHE, imagen = TipoImagen.SIN_IMAGEN.value) }
                )
                loadCochesFromRepository() // Actualizamos la lista
            }
        }
    }

    // carga en el estado el alumno seleccionado
    fun updateCocheSeleccionado(coche: Coche) {
        logger.debug { "Actualizando estado de Coche: $coche" }

        lateinit var fileImage: File
        lateinit var imagen: Image

        storage.loadImage(coche.imagen).onSuccess {
            imagen = Image(it.absoluteFile.toURI().toString())
            fileImage = it
        }.onFailure {
            imagen = Image(RoutesManager.getResourceAsStream(SIN_IMAGEN))
            fileImage = File(RoutesManager.getResource(SIN_IMAGEN).toURI())
        }

        val cocheSeleccionado = CocheFormulario(
            numero = coche.id,
            matricula = coche.matricula,
            marca = coche.marca,
            modelo = coche.modelo,
            tipoMotor = coche.tipoMotor.name,
            fechaMatriculacion = coche.fechaMatriculacion,
            fileImage = fileImage,
            imagen = imagen
        )

        state.value = state.value.copy(cocheSeleccionado = cocheSeleccionado)
    }


    // Crea un nuevo coche en el estado y repositorio
    fun crearCoche(cocheNuevo: CocheFormulario): Result<Coche, CocheError> {
        logger.debug { "Creando Coche" }
        // creamos el coche
        println("Coche a crear: $cocheNuevo")
        var newCoche = cocheNuevo.toModel().copy(id = Coche.NEW_COCHE)
        return newCoche.validate()
            .andThen {
                // Copiamos la imagen si no es nula
                println("Imagen a copiar: ${cocheNuevo.fileImage}")
                cocheNuevo.fileImage?.let { newFileImage ->
                    storage.saveImage(newFileImage).onSuccess {
                        println("Imagen copiada: ${it.name}")
                        newCoche = newCoche.copy(imagen = it.name)
                    }
                }
                val new = repository.save(newCoche)
                // Actualizamos la lista
                // Podriamos cargar del repositorio otra vez, si fuera concurente o
                // conectada a un servidor remoto debería hacerlo así
                updateState(state.value.coches + new)
                Ok(new)
            }
    }

    // Edita un coche en el estado y repositorio
    fun editarCoche(cocheEditado: CocheFormulario): Result<Coche, CocheError> {
        logger.debug { "Editando Coche" }
        // creamos el coche
        val fileImageTemp = state.value.cocheSeleccionado.fileImage // Nombre de la imagen que tiene
        var updatedCoche = cocheEditado.toModel().copy(imagen = fileImageTemp!!.name)
        return updatedCoche.validate()
            .andThen {
                // Tenemos dos opciones, que no tuviese imagen o que si la tuviese
                cocheEditado.fileImage?.let { newFileImage ->
                    if (updatedCoche.imagen == TipoImagen.SIN_IMAGEN.value || updatedCoche.imagen == TipoImagen.EMPTY.value) {
                        storage.saveImage(newFileImage).onSuccess {
                            updatedCoche = updatedCoche.copy(imagen = it.name)
                        }
                    } else {
                        storage.updateImage(fileImageTemp, newFileImage)
                    }
                }
                val updated = repository.save(updatedCoche)
                // Actualizamos la lista
                // Podriamos cargar del repositorio otra vez, si fuera concurente o
                // conectada a un servidor remoto debería hacerlo así
                //val lista = state.value.alumnos.toMutableList()
                //val indexedValue = lista.indexOfFirst { a -> a.id == updated.id }
                //lista[indexedValue] = updated
                // updateState(lista)
                updateState(
                    state.value.coches.filter { it.id != updated.id } + updated
                )
                Ok(updated)
            }
    }

    // Elimina un coche en el estado y repositorio
    fun eliminarCoche(): Result<Unit, CocheError> {
        logger.debug { "Eliminando Coche" }
        // Hay que eliminar su imagen, primero siempre una copia!!!
        val coche = state.value.cocheSeleccionado.copy()
        // Para evitar que cambien en la selección!!!

        coche.fileImage?.let {
            if (it.name != TipoImagen.SIN_IMAGEN.value) {
                storage.deleteImage(it)
            }
        }

        // Borramos del repositorio
        repository.deleteById(coche.numero)
        // Actualizamos la lista
        // Podriamos cargar del repositorio otra vez, si fuera concurente o
        // conectada a un servidor remoto debería hacerlo así
        updateState(state.value.coches.filter { it.id != coche.numero })
        return Ok(Unit)
    }

    fun exportToZip(fileToZip: File): Result<Unit, CocheError> {
        logger.debug { "Exportando a ZIP: $fileToZip" }
        // recogemos los coches del repositorio
        val coches = repository.findAll()
        storage.exportToZip(fileToZip, coches)
        return Ok(Unit)
    }

    fun loadCochesFromZip(fileToUnzip: File): Result<List<Coche>, CocheError> {
        logger.debug { "Importando de ZIP: $fileToUnzip" }
        // recogemos los coches del repositorio
        return storage.loadFromZip(fileToUnzip).onSuccess { it ->
            repository.deleteAll()
            repository.saveAll(it.map { a -> a.copy(id = Coche.NEW_COCHE) })
            loadCochesFromRepository()
        }
    }

    fun setTipoOperacion(tipo: TipoOperacion) {
        logger.debug { "Cambiando tipo de operación: $tipo" }
        state.value = state.value.copy(tipoOperacion = tipo)
    }

    fun getDefautltImage(): Image {
        return Image(RoutesManager.getResourceAsStream(SIN_IMAGEN))
    }

    // Mi estado
    // Enums
    enum class TipoFiltro(val value: String) {
        TODOS("Todos/as"),
        GASOLINA("Gasolina"),
        DIESEL("Diésel"),
        HIBRIDO("Híbrido"),
        ELECTRICO("Eléctrico")
    }

    enum class TipoOperacion(val value: String) {
        NUEVO("Nuevo"), EDITAR("Editar")
    }

    enum class TipoImagen(val value: String) {
        SIN_IMAGEN("sin-imagen.png"), EMPTY("")
    }

    // Clases que representan el estado
    // Estado del ViewModel
    data class ConcesionarioState(
        // Los contenedores de colecciones deben ser ObservableList
        val typesMotor: List<String> = listOf(),
        val coches: List<Coche> = listOf(),

        // siempre que cambia el tipo de operacion, se actualiza el coche
        val cocheSeleccionado: CocheFormulario = CocheFormulario(), // Coche seleccionado en tabla
        val tipoOperacion: TipoOperacion = TipoOperacion.NUEVO // Tipo de operacion
    )

    // Estado para formularios de Coche (seleccionado y de operaciones)
    data class CocheFormulario(
        val numero: Long = Coche.NEW_COCHE,
        val matricula: String = "",
        val marca: String = "",
        val modelo: String = "",
        val tipoMotor: String = "",
        val fechaMatriculacion: LocalDate = LocalDate.now(),
        val imagen: Image = Image(RoutesManager.getResourceAsStream(SIN_IMAGEN)),
        var fileImage: File? = null
    )
}
