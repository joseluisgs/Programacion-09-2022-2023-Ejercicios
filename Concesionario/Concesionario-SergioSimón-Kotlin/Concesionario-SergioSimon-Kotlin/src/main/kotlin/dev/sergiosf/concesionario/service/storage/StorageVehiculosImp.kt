package dev.sergiosf.concesionario.service.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.concesionario.config.AppConfig
import dev.sergiosf.concesionario.errors.VehiculoError
import dev.sergiosf.concesionario.models.Vehiculo
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant

private val logger = KotlinLogging.logger {}

class StorageVehiculosImp(
    private val appConfig: AppConfig
): StorageVehiculos, KoinComponent {

    init {
        logger.debug { "Creando directorio de imagenes si no existe" }
        Files.createDirectories(Paths.get(appConfig.imagesDirectory))
    }

    private fun getImagenName(newFileImage: File): String {
        val name = newFileImage.name
        val extension = name.substring(name.lastIndexOf(".") + 1)
        return "${Instant.now().toEpochMilli()}.$extension"
    }

    override fun saveImage(fileName: File): Result<File, VehiculoError> {
        logger.debug { "Guardando imagen $fileName" }
        return try {
            val newFileImage = File(appConfig.imagesDirectory + getImagenName(fileName))
            Files.copy(fileName.toPath(), newFileImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        } catch (e: Exception) {
            Err(VehiculoError.DeleteImage("Error al guardar la imagen: ${e.message}"))
        }
    }

    override fun loadImage(fileName: String): Result<File, VehiculoError> {
        logger.debug { "Cargando imagen $fileName" }
        val file = File(appConfig.imagesDirectory + fileName)
        return if (file.exists()) {
            Ok(file)
        } else {
            Err(VehiculoError.LoadImage("La imagen no existe: ${file.name}"))
        }
    }

    override fun deleteImage(fileImage: File): Result<Unit, VehiculoError> {
        logger.debug { "Borrando imagen $fileImage" }
        Files.deleteIfExists(fileImage.toPath())
        return Ok(Unit)
    }

    override fun deleteAllImages(): Result<Long, VehiculoError> {
        logger.debug { "Borrando todas las imagenes" }
        return try {
            Ok(Files.walk(Paths.get(appConfig.imagesDirectory))
                .filter { Files.isRegularFile(it) }
                .map { Files.deleteIfExists(it) }
                .count())
        } catch (e: Exception) {
            Err(VehiculoError.DeleteImage("Error al borrar todas las imagenes: ${e.message}"))
        }
    }

    override fun updateImage(fileImage: File, newFileImage: File): Result<File, VehiculoError> {
        logger.debug { "Actualizando imagen ${fileImage.name}" }
        return try {
            val newUpdateImage = File(appConfig.imagesDirectory + fileImage.name)
            Files.copy(newFileImage.toPath(), newUpdateImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newUpdateImage)
        } catch (e: Exception) {
            Err(VehiculoError.SaveImage("Error al guardar la imagen: ${e.message}"))
        }
    }
}