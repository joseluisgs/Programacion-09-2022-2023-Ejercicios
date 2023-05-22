package com.example.concesionario.services.storage.vehicle

import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.errors.VehicleError
import com.example.concesionario.mappers.toClass
import com.example.concesionario.mappers.toDto
import com.example.concesionario.models.Vehicle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.example.concesionario.config.AppConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.lang.Exception

class VehicleFileJson: VehicleStorage, KoinComponent {
    private val appConfig: AppConfig by inject()

    private val localPath = "${appConfig.appData}${File.separator}concesionario.json"

    override fun saveAll(elements: List<Vehicle>): Result<List<Vehicle>, VehicleError> {
        val file = File(localPath)
        if (file.exists() && !file.canWrite()) return Err(VehicleError.ExportError("JSON"))
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(elements.map { it.toDto() })
            file.writeText(jsonString)
            Ok(elements)
        }catch (e: Exception){
            Err(VehicleError.ExportError("JSON"))
        }
    }

    override fun loadAll(): Result<List<Vehicle>, VehicleError> {
        val file = File(localPath)
        if (!file.exists() || !file.canRead()) return Err(VehicleError.ImportError("JSON"))
        val gson = GsonBuilder().setPrettyPrinting().create()
        val importType = object : TypeToken<List<VehicleDto>>() {}.type
        return try {
            val jsonString = file.readText()
            val data = gson.fromJson<List<VehicleDto>>(jsonString, importType)
            Ok(data.map { it.toClass() })
        } catch (e: Exception) {
            Err(VehicleError.ImportError("JSON"))
        }
    }
}