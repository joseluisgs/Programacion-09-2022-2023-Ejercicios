package com.example.gestionvehiculosconimagenes_kotlin.services.repository

import com.example.gestionvehiculosconimagenes_kotlin.mapper.toVehiculo
import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.IVehiculoRepository
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseSQLDelight
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
internal class VehiculoRepositoryImplTest {

    @MockK
    lateinit var database: DatabaseSQLDelight

    @InjectMockKs
    lateinit var repository: IVehiculoRepository

    init {
        MockKAnnotations.init(io.mockk.MockKDsl)
    }

    val vehiculos = listOf<Vehiculo>(
        Vehiculo(1, "AAAA465", "Peugeot", TipoMotor.GASOLINA, 574.3),
        Vehiculo(2, "ZZZ645", "Toyota", TipoMotor.DIESEL, 756.34)
    )

    @Test
    fun getByMarcaModelo() {
    }

    @Test
    fun getByTipoMotor() {
    }

    @Test
    fun getAll() {
        every { database.queries.selectAllVehiculos().executeAsList().map { it.toVehiculo() } } returns vehiculos
        val vehiculosGet = repository.getAll()

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