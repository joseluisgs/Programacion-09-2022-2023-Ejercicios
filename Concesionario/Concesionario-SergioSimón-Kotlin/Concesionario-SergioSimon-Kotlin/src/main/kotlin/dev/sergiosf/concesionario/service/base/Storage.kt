package dev.sergiosf.concesionario.service.base

interface Storage<T> {
    fun loadData(): List<T>
    fun saveData(data: List<T>)
}