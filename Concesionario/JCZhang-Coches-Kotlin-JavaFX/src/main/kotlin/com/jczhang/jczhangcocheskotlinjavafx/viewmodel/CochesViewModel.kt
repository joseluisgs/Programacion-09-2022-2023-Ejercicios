package com.jczhang.jczhangcocheskotlinjavafx.viewmodel

import com.jczhang.jczhangcocheskotlinjavafx.models.Coche
import com.jczhang.jczhangcocheskotlinjavafx.repositories.coches.CrudRepositoryImpl
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.time.LocalDate


private val logger = KotlinLogging.logger { }

class CochesViewModel(private val repository: CrudRepositoryImpl) : KoinComponent {

    init {
        //añado los coches por defecto a la base de datos
        val defaultCarOne = Coche(
            id = null,
            marca = "Tesla",
            modelo = "Model3",
            tipoMotor = "GASOLINA",
            km = 2.55,
            matriculacion = LocalDate.now()
        )
        val defaultCarTwo = Coche(
            id = null,
            marca = "Ferrari",
            modelo = "B-1341",
            tipoMotor = "ELECTRICO",
            km = 5.15,
            matriculacion = LocalDate.now()
        )
        val defaultCarThree = Coche(
            id = null,
            marca = "Citroen",
            modelo = "A-1234",
            tipoMotor = "HIBRIDO",
            km = 7234.6,
            matriculacion = LocalDate.now()
        )
        val defaultCarFour = Coche(
            id = null,
            marca = "BMV",
            modelo = "B-5132",
            tipoMotor = "DIESEL",
            km = 5.15,
            matriculacion = LocalDate.now()
        )
        val defaultCarFive = Coche(
            id = null,
            marca = "Toyota",
            modelo = "C-123f",
            tipoMotor = "GASOLINA",
            km = 6.23,
            matriculacion = LocalDate.now()
        )
        addIntoDatabase(defaultCarOne)
        addIntoDatabase(defaultCarTwo)
        addIntoDatabase(defaultCarThree)
        addIntoDatabase(defaultCarFour)
        addIntoDatabase(defaultCarFive)
    }

    val state = ConcesionarioState()
    val listaTipos = listOf("GASOLINA", "DIESEL", "ELECTRICO", "HIBRIDO", "TODO")
    val listaCoches = loadFromDatabase()

    fun updateState(newValue: Coche){
        logger.debug { "Actualizando estado del coche" }
        state.cocheSeleccionado.id.value = newValue.id.toString()
        state.cocheSeleccionado.marca.value = newValue.marca
        state.cocheSeleccionado.modelo.value = newValue.modelo
        state.cocheSeleccionado.tipoMotor.value = newValue.tipoMotor
        state.cocheSeleccionado.km.value = newValue.km.toString()
        state.cocheSeleccionado.matriculacion.value = newValue.matriculacion
    }


    fun filteredCochesList(tipo: String, cocheBuscado: String): FilteredList<Coche> {
        logger.debug { "Filtrando lista de coches con tipo $tipo y búsqueda $cocheBuscado" }
        val cochesList = state.coches
        val filteredList = FilteredList(cochesList)

        filteredList.setPredicate { coche ->
            val tipoMatch = tipo.isEmpty() || tipo.equals("TODO", ignoreCase = true) || coche.tipoMotor.contains(
                tipo,
                ignoreCase = true
            )
            val cocheMatch = coche.modelo.contains(cocheBuscado, ignoreCase = true)
            tipoMatch && cocheMatch
        }

        return filteredList
    }


    fun addIntoDatabase(item: Coche) {
        repository.addToDataBase(item)
    }

    fun loadFromDatabase() {
        state.coches.setAll(repository.loadFromDataBase())
        updateActualState()
    }

    fun editCar(coche: Coche) {
        logger.debug { "Editando coche" }
        val id = coche.id
        val marca = coche.marca
        val modelo = coche.modelo
        val tipo = coche.tipoMotor
        val km = coche.km
        val matriculacion = coche.matriculacion.toString()
        repository.updateItem(id, marca, modelo, tipo, km, matriculacion)
    }

    private fun updateActualState() {
        state.cocheSeleccionado.limpiar()
        state.cocheOperacion.limpiar()
    }

    fun deleteFromDataBase(coche: Long?) {

        return repository.deleteFromDatabase(coche)
    }

    data class ConcesionarioState(
        val coches: ObservableList<Coche> = FXCollections.observableArrayList<Coche>(),
        val cocheSeleccionado: CochesState = CochesState(),
        val cocheOperacion: CochesState = CochesState(),

        )
}

data class CochesState(
    var id: SimpleStringProperty = SimpleStringProperty(""),
    var marca: SimpleStringProperty = SimpleStringProperty(""),
    var modelo: SimpleStringProperty = SimpleStringProperty(""),
    var tipoMotor: SimpleStringProperty = SimpleStringProperty(""),
    var km: SimpleStringProperty = SimpleStringProperty(""),
    var matriculacion: SimpleObjectProperty<LocalDate> = SimpleObjectProperty(LocalDate.now()),

    ) {
    fun limpiar() {
        matriculacion.value = null
        km.value = ""
        tipoMotor.value = ""
        marca.value = ""
        modelo.value = ""
        id.value = ""
    }

}
