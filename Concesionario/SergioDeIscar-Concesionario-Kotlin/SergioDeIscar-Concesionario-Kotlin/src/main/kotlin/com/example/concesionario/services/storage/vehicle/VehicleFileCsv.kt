package com.example.concesionario.services.storage.vehicle

import com.example.concesionario.config.AppConfig
import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.errors.VehicleError
import com.example.concesionario.mappers.toClass
import com.example.concesionario.models.Vehicle
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class VehicleFileCsv: VehicleStorage, KoinComponent {
    private val appConfig: AppConfig by inject()

    private val localPath = "${appConfig.appData}${File.separator}concesionario.csv"

    override fun saveAll(elements: List<Vehicle>): Result<List<Vehicle>, VehicleError> {
        val file = File(localPath)
        if (file.exists() && !file.canWrite()) return Err(VehicleError.ExportError("CSV"))
        return try {
            file.writeText("id,marca,modelo,motor,fechaMatriculacion,imagenUrl\n")
            elements.forEach {
                file.appendText(
                    "${it.id},${it.marca},${it.modelo},${it.motor.value},${it.fechaMatriculacion},${it.imagenUrl}\n"
                )
            }
            Ok(elements)
        }catch (e: Exception){
            Err(VehicleError.ExportError("CSV"))
        }
    }

    override fun loadAll(): Result<List<Vehicle>, VehicleError> {
        val file = File(localPath)
        if (!file.exists() || !file.canRead()) return Err(VehicleError.ImportError("CSV"))
        return try {
            Ok(
                file.readLines()
                    .drop(1)
                    .map { it.split(",") }
                    .map { celda ->
                        VehicleDto(
                            celda[0],
                            celda[1],
                            celda[2],
                            celda[3],
                            celda[4],
                            celda[5]
                        ).toClass()
                    }
            )
        }catch (e: Exception){
            Err(VehicleError.ImportError("CSV"))
        }
    }
}