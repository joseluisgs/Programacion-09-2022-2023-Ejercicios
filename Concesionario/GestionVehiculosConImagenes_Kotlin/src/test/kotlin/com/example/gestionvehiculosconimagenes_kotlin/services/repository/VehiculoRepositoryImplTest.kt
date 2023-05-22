package com.example.gestionvehiculosconimagenes_kotlin.services.repository

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.VehiculoRepositoryMySQL
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.VehiculoRepositorySQLDelight
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseMySQL
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseSQLDelight
import database.DatabaseQueries
import database.VehiculoEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class VehiculoRepositoryImplTest {

    @Mock
    lateinit var database: DatabaseMySQL

    @InjectMocks
    lateinit var repository: VehiculoRepositoryMySQL


    val vehiculos = listOf<Vehiculo>(
        Vehiculo(1, "AAAA465", "Peugeot", "Gris", TipoMotor.GASOLINA, 574.3),
        Vehiculo(2, "ZZZ645", "Toyota", "Azul", TipoMotor.DIESEL, 756.34)
    )

    val vehiculosEntities = listOf<VehiculoEntity>(
        VehiculoEntity(1, "AAAA465", "Peugeot", "Gris", TipoMotor.GASOLINA.toString(), 574.3, LocalDate.now().toString(), TipoMotor.GASOLINA.imagePath),
        VehiculoEntity(2, "ZZZ645", "Toyota", "Azul", TipoMotor.DIESEL.toString(), 756.34, LocalDate.now().toString(), TipoMotor.GASOLINA.imagePath)
    )

    @Test
    fun getByMarcaModelo() {
    }

    @Test
    fun getByTipoMotor() {
    }

    @Test
    fun getAll() {
       whenever( database.selectAllVehiculos() ).thenReturn(vehiculos)
        val vehiculosGet = repository.getAll()
        assertEquals(2, vehiculosGet.size)
        assertEquals(2, vehiculosGet.size)
        verify(database, times(1)).selectAllVehiculos()
    }

    @Test
    fun getById() {
    }

    @Test
    fun save() {
    }

    @Test
    fun saveAll() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun deleteAll() {
    }
}