package com.example.vehiculoscrudinterface.repositories

interface CrudRepository <T, ID> {

    fun buscarTodos(): List<T>
    fun buscarPorId(id: ID): T?
    fun eliminarTodos()
    fun eliminarPorId(id: ID): T?
    fun salvar(item: T): T

}