package dev.sbytmacke.vehiculositv.services.storages

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import dev.sbytmacke.vehiculositv.errors.VehiculeError
import dev.sbytmacke.vehiculositv.models.Vehicule
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

class VehiculesStorageImpl : VehiculesStorage {

    override fun exportToJson(file: File, vehicules: List<Vehicule?>): Result<Long, VehiculeError> {
        logger.debug { "Storage: Escribiendo en JSON" }

        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(vehicules)
            file.writeText(jsonString)
        } catch (e: Exception) {
            return Err(VehiculeError.ExportInvalid())
        }

        return Ok(1L)
    }
}