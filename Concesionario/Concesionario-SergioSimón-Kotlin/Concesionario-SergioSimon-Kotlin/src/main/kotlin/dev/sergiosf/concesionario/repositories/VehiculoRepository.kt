package dev.sergiosf.concesionario.repositories

import dev.sergiosf.concesionario.models.Vehiculo


interface VehiculoRepository {
    fun findAll(): List<Vehiculo>
    fun findById(id: Long): Vehiculo?
    fun save(vehiculos: Vehiculo): Vehiculo
    fun deleteById(id: Long)
    fun findByMatricula(matricula: String): Vehiculo?
    fun deleteAll()
    fun saveAll(vehiculos: List<Vehiculo>): List<Vehiculo>
}