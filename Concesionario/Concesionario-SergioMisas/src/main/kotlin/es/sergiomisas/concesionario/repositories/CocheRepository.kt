package es.sergiomisas.concesionario.repositories

import es.sergiomisas.concesionario.models.Coche

interface CocheRepository {
    fun findAll(): List<Coche>
    fun findById(id: Long): Coche?
    fun save(coche: Coche): Coche
    fun deleteById(id: Long)
    fun deleteAll()
    fun saveAll(coches: List<Coche>): List<Coche>
    fun findByMatricula(matricula: String): Coche?
}
