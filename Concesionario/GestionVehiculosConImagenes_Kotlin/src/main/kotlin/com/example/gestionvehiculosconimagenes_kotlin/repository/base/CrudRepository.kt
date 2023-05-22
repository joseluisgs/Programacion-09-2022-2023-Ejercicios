package com.example.gestionvehiculosconimagenes_kotlin.repository.base

interface CrudRepository<T, ID> {
    fun getAll(): List<T>
    fun getById(id: ID): T?
    fun save(entity: T): T
    fun saveAll(entities: List<T>): List<T>
    fun delete(id: ID)
    fun deleteAll()
}