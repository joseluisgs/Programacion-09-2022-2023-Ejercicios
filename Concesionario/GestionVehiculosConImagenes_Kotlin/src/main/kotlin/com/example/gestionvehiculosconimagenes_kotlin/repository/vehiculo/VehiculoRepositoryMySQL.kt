package com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseMySQL
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

class VehiculoRepositoryMySQL(
    private val database: DatabaseMySQL
) : IVehiculoRepository{
    override fun getByMarcaModelo(marcaModelo: String): List<Vehiculo> {
        logger.debug { "Consigo todos los vehiculos según la marca y modelo $marcaModelo" }
        return database.selectAllVehiculos()
            .filter { it.marcaModelo.lowercase().contains(marcaModelo) }
    }

    override fun getByTipoMotor(tipoMotor: TipoMotor): List<Vehiculo> {
        logger.debug { "Consigo todos los vehículos según el tipo de motor $tipoMotor" }
        return database.selectAllVehiculos()
            .filter { it.tipoMotor == tipoMotor }
    }

    override fun getAll(): List<Vehiculo> {
        logger.debug { "Consigo todos los vehículos" }
        return database.selectAllVehiculos()
    }

    override fun getById(id: Long): Vehiculo? {
        logger.debug { "Consigo el vehiculo de id $id" }
        return database.selectVehiculoById(id)
    }

    override fun save(entity: Vehiculo): Vehiculo {
        logger.debug { "Almaceno la información de un vehículo" }
        return getById(entity.id)?.let{
            updateVehiculo(entity)
        }?:run{
            createVehiculo(entity)
        }
    }

    private fun createVehiculo(entity: Vehiculo): Vehiculo {
        logger.debug { "Cargamos un nuevo vehiculo en la base de datos" }
        return database.insertVehiculo(entity)
    }

    private fun updateVehiculo(entity: Vehiculo): Vehiculo {
        logger.debug { "Actualizamos la info de un vehículo" }
        return database.updateVehiculo(entity)
    }

    override fun saveAll(entities: List<Vehiculo>): List<Vehiculo> {
        logger.debug { "Almacenamos el conjunto de coches dados" }
        entities.map { save(it) }
        return entities
    }

    override fun delete(id: Long) {
        logger.debug { "Eliminamos el vehículo de id $id" }
        database.deleteVehiculo(id)
    }

    override fun deleteAll() {
        logger.debug { "Eliminamos todos los vehículos de la base de datos" }
        database.deleteAllVehiculos()
    }
}