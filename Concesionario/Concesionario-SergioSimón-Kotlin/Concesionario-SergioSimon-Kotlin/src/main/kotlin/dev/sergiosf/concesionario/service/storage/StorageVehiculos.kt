package dev.sergiosf.concesionario.service.storage

import com.github.michaelbull.result.Result
import dev.sergiosf.concesionario.errors.VehiculoError
import java.io.File

interface StorageVehiculos {
    fun saveImage(fileName: File): Result<File, VehiculoError>
    fun loadImage(fileName: String): Result<File, VehiculoError>
    fun deleteImage(fileImage: File): Result<Unit, VehiculoError>
    fun deleteAllImages(): Result<Long, VehiculoError>
    fun updateImage(fileImage: File, newFileImage: File): Result<File, VehiculoError>
}