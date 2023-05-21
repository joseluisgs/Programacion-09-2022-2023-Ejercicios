package com.jczhang.jczhangcocheskotlinjavafx.repositories.base

interface CrudRepository<T> {

    fun addToDataBase(item: T)
    fun loadFromDataBase(): List<T>
    fun updateItem(id: Long?, marca: String, modelo: String, tipo: String, km: Double, fecha: String)
    fun deleteFromDatabase(idBuscado: Long?)
}