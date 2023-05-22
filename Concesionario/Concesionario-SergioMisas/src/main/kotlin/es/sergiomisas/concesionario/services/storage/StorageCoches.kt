package es.sergiomisas.concesionario.services.storage

import com.github.michaelbull.result.Result
import es.sergiomisas.concesionario.errors.CocheError
import es.sergiomisas.concesionario.models.Coche
import java.io.File

interface StorageCoches {
    fun storeDataJson(file: File, data: List<Coche>): Result<Long, CocheError>
    fun loadDataJson(file: File): Result<List<Coche>, CocheError>
    fun saveImage(fileName: File): Result<File, CocheError>
    fun loadImage(fileName: String): Result<File, CocheError>
    fun deleteImage(fileName: File): Result<Unit, CocheError>
    fun deleteAllImages(): Result<Long, CocheError>
    fun updateImage(fileImage: File, newFileImage: File): Result<File, CocheError>
    fun exportToZip(fileToZip: File, data: List<Coche>): Result<File, CocheError>
    fun loadFromZip(fileToUnzip: File): Result<List<Coche>, CocheError>
}
