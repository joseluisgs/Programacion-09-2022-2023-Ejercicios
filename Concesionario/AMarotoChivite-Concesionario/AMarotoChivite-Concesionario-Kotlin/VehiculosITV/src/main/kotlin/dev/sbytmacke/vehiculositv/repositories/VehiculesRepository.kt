package dev.sbytmacke.vehiculositv.repositories

import dev.sbytmacke.vehiculositv.models.Vehicule

interface VehiculesRepository {
    fun findAll(): List<Vehicule>
    fun save(item: Vehicule): Vehicule
    fun deleteById(id: Long)
    fun deleteAll()
    fun saveAll(item: List<Vehicule>): List<Vehicule>
    fun getByMatricule(matricule: String): Vehicule?
    fun getById(id: Long): Vehicule?
    fun existItem(item: Vehicule): Boolean
}