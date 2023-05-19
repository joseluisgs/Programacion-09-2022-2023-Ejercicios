package dev.joseluisgs.expedientesacademicos.services.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.joseluisgs.expedientesacademicos.config.AppConfig
import dev.joseluisgs.expedientesacademicos.dto.json.CocheDto
import dev.joseluisgs.expedientesacademicos.errors.CocheError
import dev.joseluisgs.expedientesacademicos.mappers.toDto
import dev.joseluisgs.expedientesacademicos.mappers.toModel
import dev.joseluisgs.expedientesacademicos.models.Coche
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.io.path.name

private val logger = KotlinLogging.logger {}

class StorageCochesImpl(
    private val appConfig: AppConfig
) : StorageCoches {

    init {
        // Creamos el directorio de imagenes si no existe
        logger.debug { "Creando directorio de imagenes si no existe" }
        Files.createDirectories(Paths.get(appConfig.imagesDirectory))
    }

    override fun storeDataJson(file: File, data: List<Coche>): Result<Long, CocheError> {
        logger.debug { "Guardando datos en fichero $file" }
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data.toDto())
            file.writeText(jsonString)
            Ok(data.size.toLong())
        } catch (e: Exception) {
            Err(CocheError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }

    }

    override fun loadDataJson(file: File): Result<List<Coche>, CocheError> {
        logger.debug { "Cargando datos en fichero $file" }
        val gson = GsonBuilder().setPrettyPrinting().create()
        // Debemos decirle el tipo de datos que queremos parsear
        val importType = object : TypeToken<List<CocheDto>>() {}.type
        return try {
            val jsonString = file.readText()
            val data = gson.fromJson<List<CocheDto>>(jsonString, importType)
            Ok(data.toModel())
        } catch (e: Exception) {
            Err(CocheError.LoadJson("Error al parsear el JSON: ${e.message}"))
        }

    }

    private fun getImagenName(newFileImage: File): String {
        val name = newFileImage.name
        val extension = name.substring(name.lastIndexOf(".") + 1)
        return "${Instant.now().toEpochMilli()}.$extension"
    }

    override fun saveImage(fileName: File): Result<File, CocheError> {
        logger.debug { "Guardando imagen $fileName" }
        return try {
            val newFileImage = File(appConfig.imagesDirectory + getImagenName(fileName))
            Files.copy(fileName.toPath(), newFileImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        } catch (e: Exception) {
            Err(CocheError.SaveImage("Error al guardar la imagen: ${e.message}"))
        }
    }

    override fun loadImage(fileName: String): Result<File, CocheError> {
        logger.debug { "Cargando imagen $fileName" }
        val file = File(appConfig.imagesDirectory + fileName)
        return if (file.exists()) {
            Ok(file)
        } else {
            Err(CocheError.LoadImage("La imagen no existe: ${file.name}"))
        }
    }

    override fun deleteImage(fileName: File): Result<Unit, CocheError> {
        logger.debug { "Borrando imagen $fileName" }
        Files.deleteIfExists(fileName.toPath())
        return Ok(Unit)
    }

    override fun deleteAllImages(): Result<Long, CocheError> {
        logger.debug { "Borrando todas las imagenes" }
        return try {
            Ok(Files.walk(Paths.get(appConfig.imagesDirectory))
                .filter { Files.isRegularFile(it) }
                .map { Files.deleteIfExists(it) }
                .count())
        } catch (e: Exception) {
            Err(CocheError.DeleteImage("Error al borrar todas las imagenes: ${e.message}"))
        }
    }

    override fun updateImage(fileImage: File, newFileImage: File): Result<File, CocheError> {
        logger.debug { "Actualizando imagen ${fileImage.name}" }
        return try {
            val newUpdateImage = File(appConfig.imagesDirectory + fileImage.name)
            Files.copy(newFileImage.toPath(), newUpdateImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newUpdateImage)
        } catch (e: Exception) {
            Err(CocheError.SaveImage("Error al guardar la imagen: ${e.message}"))
        }
    }

    override fun exportToZip(fileToZip: File, data: List<Coche>): Result<File, CocheError> {
        logger.debug { "Exportando a ZIP $fileToZip" }
        // Creamos el directorio temporal
        val tempDir = Files.createTempDirectory("temp")
        // copiamos todas las imagenes al directorio temporal
        return try {

            data.forEach {
                val file = File(appConfig.imagesDirectory + it.imagen)
                if (file.exists()) {
                    Files.copy(
                        file.toPath(),
                        Paths.get(tempDir.toString(), file.name),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
            // exportamos un json con el listado al directorio temporal
            storeDataJson(File("$tempDir/data.json"), data)
            // Listamos por consola el contenido del directorio temporal
            Files.walk(tempDir).forEach { println(it) }
            // Eliminamos el directorio temporal al terminar
            // comprimimos
            val archivos = Files.walk(tempDir)
                .filter { Files.isRegularFile(it) }
                .toList()
            ZipOutputStream(Files.newOutputStream(fileToZip.toPath())).use { zip ->
                archivos.forEach { archivo ->
                    val entradaZip = ZipEntry(tempDir.relativize(archivo).toString())
                    zip.putNextEntry(entradaZip)
                    Files.copy(archivo, zip)
                    zip.closeEntry()
                }
            }
            tempDir.toFile().deleteRecursively()
            Ok(fileToZip)
        } catch (e: Exception) {
            logger.error { "Error al exportar a ZIP: ${e.message}" }
            Err(CocheError.ExportZip("Error al exportar a ZIP: ${e.message}"))
        }
    }

    override fun loadFromZip(fileToUnzip: File): Result<List<Coche>, CocheError> {
        logger.debug { "Importando desde ZIP $fileToUnzip" }
        // Creamos el directorio temporal
        val tempDir = Files.createTempDirectory("temp")
        return try {
            // Descomprimimos a un directorio temporal
            ZipFile(fileToUnzip).use { zip ->
                zip.entries().asSequence().forEach { entrada ->
                    zip.getInputStream(entrada).use { input ->
                        Files.copy(
                            input,
                            Paths.get(tempDir.toString(), entrada.name),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    }
                }
            }

            Files.walk(tempDir).forEach {
                // copiamos todas las imagenes, es decir, lo que no es .json al directorio de imagenes
                if (!it.toString().endsWith(".json") && Files.isRegularFile(it)) {
                    Files.copy(
                        it,
                        Paths.get(appConfig.imagesDirectory, it.name),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
            // tomamos el fichero data.json y lo parseamos a una lista de alumnos
            val data = loadDataJson(File("$tempDir/data.json"))
            tempDir.toFile().deleteRecursively()
            return data
        } catch (e: Exception) {
            logger.error { "Error al importar desde ZIP: ${e.message}" }
            Err(CocheError.ImportZip("Error al importar desde ZIP: ${e.message}"))
        }
    }
}
