package dev.sbytmacke.vehiculositv.services.storages

import com.github.michaelbull.result.Result
import dev.sbytmacke.vehiculositv.errors.VehiculeError
import dev.sbytmacke.vehiculositv.models.Vehicule
import java.io.File

interface VehiculesStorage {
    // CRUD vehicules
    fun exportToJson(file: File, vehicules: List<Vehicule?>): Result<Long, VehiculeError>

    // TODO: CRUD imagen
    /*    fun getImagenName(newFileImage: File): String
        fun saveImage(fileName: File): Result<File, Any>
        fun loadImage(fileName: String): Result<File, Any>
        fun deleteImage(fileImage: File): Result<Unit, Any>
        fun updateImage(fileImage: File, newFileImage: File): Result<File, Any>*/
}