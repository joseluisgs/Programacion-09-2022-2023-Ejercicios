package dev.kevin.vehiculosfx.services.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kevin.vehiculosfx.config.ConfigApp
import dev.kevin.vehiculosfx.errors.VehiculoError
import mu.KotlinLogging
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant


val logger = KotlinLogging.logger {  }
class StorageVehiculosImpl(val configApp: ConfigApp) : StorageVehiculos {

    private fun getImageName(newFileImage: File): String{
        val name = newFileImage.name
        val extension = name.substring(name.lastIndexOf(".") + 1)
        return ("${Instant.now().toEpochMilli()}.$extension")
    }

    override fun saveImage(fileName: File): Result<File, VehiculoError> {
        logger.debug { "Guardando la imagen $fileName" }
        return try{
            val newFileImage = File(configApp.APP_IMAGES + getImageName(fileName))
            Files.copy(fileName.toPath(), newFileImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        }catch(e: Exception){
            Err(VehiculoError.SaveImage("Error al guradar la imagen: ${e.message}"))
        }
    }

    override fun loadImage(fileName: String): Result<File, VehiculoError> {
        logger.debug{ "Cargando la images $fileName" }
        val file = File(configApp.APP_IMAGES + fileName)
        return if(file.exists()){
            Ok(file)
        }else{
            Err(VehiculoError.SaveImage("La imagen no existe: ${file.name}"))
        }
    }

    override fun deleteImage(fileName: File): Result<Unit, VehiculoError> {
        logger.debug { "Borrando la imagen $fileName" }
        Files.deleteIfExists(fileName.toPath())
        return Ok(Unit)
    }

    override fun deleteAllImages(): Result<Long, VehiculoError> {
        logger.debug { "Borrando todas las images" }
        return try{
            Ok(Files.walk(Paths.get(configApp.APP_IMAGES))
                .filter { Files.isRegularFile(it) }
                .map { Files.deleteIfExists(it) }
                .count())
        }catch (e: Exception){
            Err(VehiculoError.DeleteAllImages("Error al borrar todas la imagenes: ${e.message}"))
        }
    }

    override fun updateImage(imagenName: File, newFileImage: File): Result<File, VehiculoError> {
        logger.debug { "Actualizando la imagen $imagenName" }
        return try{
            val newUpdateImage = File(configApp.APP_IMAGES + imagenName.name)
            Files.copy(newFileImage.toPath(), newUpdateImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        }catch (e: Exception){
            Err(VehiculoError.UpdateImage("Error al actualizar la imagen ${e.message}"))
        }
    }
}