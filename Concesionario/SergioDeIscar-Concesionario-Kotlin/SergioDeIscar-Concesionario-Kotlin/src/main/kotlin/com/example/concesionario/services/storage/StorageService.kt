package services.storage

import com.github.michaelbull.result.Result

interface StorageService<T, ERR> {
    fun saveAll(elements: List<T>): Result<List<T>, ERR>
    fun loadAll(): Result<List<T>, ERR>
}