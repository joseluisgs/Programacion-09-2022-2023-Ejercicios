package repositories

import com.github.michaelbull.result.Result

interface CrudRepository<T, ID, ERR> {
    fun findAll(): Iterable<T>
    fun findById(id: ID): Result<T, ERR>
    fun save(element: T): Result<T, ERR>
    fun saveAll(elements: Iterable<T>)
    fun deleteById(id: ID): Result<Boolean, ERR>
    fun delete(element: T): Result<Boolean, ERR>
    fun deleteAll()
    fun existsById(id: ID): Boolean
}