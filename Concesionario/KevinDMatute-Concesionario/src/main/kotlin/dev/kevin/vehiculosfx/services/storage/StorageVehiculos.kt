package dev.kevin.vehiculosfx.services.storage

import com.github.michaelbull.result.Result
import dev.kevin.vehiculosfx.errors.VehiculoError
import java.io.File

interface StorageVehiculos {
    fun saveImage(fileName: File): Result <File, VehiculoError>
    fun loadImage(fileName: String): Result <File, VehiculoError>
    fun deleteImage(fileName: File): Result<Unit, VehiculoError>
    fun deleteAllImages(): Result<Long, VehiculoError>
    fun updateImage(imagenName: File, newFileImage: File): Result <File, VehiculoError>
}