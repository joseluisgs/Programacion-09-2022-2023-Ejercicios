package com.example.vehiculoscrudinterface.services.storage

interface StorageService<T> {
    fun exportar(items: List<T>)
    fun importar():List<T>
}