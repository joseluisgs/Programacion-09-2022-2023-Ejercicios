package dev.joseluisgs.expedientesacademicos.repositories

import dev.joseluisgs.expedientesacademicos.models.Coche

interface CochesRepository {
    fun findAll(): List<Coche>
    fun findById(id: Long): Coche?
    fun save(coche: Coche): Coche
    fun deleteById(id: Long)
    fun deleteAll()
    fun saveAll(alumnos: List<Coche>): List<Coche>
}
