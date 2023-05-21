package com.example.gestionvehiculosconimagenes_kotlin.service.storage

import com.example.gestionvehiculosconimagenes_kotlin.config.ConfigApp
import com.example.gestionvehiculosconimagenes_kotlin.error.VehiculoError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant

private val logger = KotlinLogging.logger {  }

class StorageImagesImpl(
    private val config: ConfigApp
): IStorageImages {
    init {

        if(config.APP_DELETE_ALL_IMAGES){
            deteleAllImages()
        }
        Files.createDirectories(Path.of(config.APP_IMAGES_PATH))
    }

    private fun getImageName(newFileImage: File): String {
        val name = newFileImage.name
        val extension = name.substring(name.lastIndexOf(".") + 1)
        return "${Instant.now().toEpochMilli()}.$extension"
    }

    override fun saveImage(fileName: File): Result<File, Unit> {
        logger.debug { "Se guarda la nueva imagen: $fileName" }
        return try {
            val newFileImage = File(config.APP_IMAGES_PATH+File.separator+getImageName(fileName))
            Files.copy(fileName.toPath(), newFileImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        } catch (e: Exception) {
            Err(Unit)
        }
    }

    override fun loadImage(fileName: String): Result<File, Unit> {
        if(fileName != "") {
            logger.debug { "Se carga la imagen de nombre $fileName" }
            val file = File(config.APP_IMAGES_PATH + File.separator + fileName)
            return if (file.exists()) {
                Ok(file)
            } else {
                Err(Unit)
            }
        }else{
            logger.debug { """No se puede cargar una imagen de nombre: "".""" }
        }
        return Err(Unit)
    }

    override fun deteleImage(fileName: File): Result<Unit, VehiculoError> {
        logger.debug { "Se elimina, si existe, la imagen $fileName" }
        Files.deleteIfExists(fileName.toPath())
        return Ok(Unit)
    }

    private fun deteleAllImages() {
        logger.debug { "Elimino todas las imagenes que ya había antes" }
        Files.walk(Paths.get(config.APP_IMAGES_PATH))
            .filter { Files.isRegularFile(it) }
            .forEach { Files.deleteIfExists(it) }
    }

    override fun updateImage(oldImage: File, newImage: File): Result<File, Unit> {
        logger.debug { "Se actualiza la imagen $oldImage, con la nueva imagen $newImage" }
        return try {
            val newUpdateImage = File(config.APP_IMAGES_PATH+File.separator+oldImage.name)
            Files.copy(newImage.toPath(), newUpdateImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newUpdateImage)
        } catch (e: Exception) {
            Err(Unit)
        }
    }
}