package dev.kevin.vehiculosfx.repositories

import dev.kevin.vehiculosfx.models.Vehiculo

interface VehiculoRepository {

    fun findAll(): List<Vehiculo>
    fun findById(id: Long): Vehiculo?
    fun save(vehiculo: Vehiculo): Vehiculo
    fun deleteById(id: Long)
    fun deleteAll()
}