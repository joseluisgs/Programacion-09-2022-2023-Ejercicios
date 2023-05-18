package dev.sbytmacke.vehiculositv.config

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.exists

private val logger = mu.KotlinLogging.logger {}

class AppConfig {

    // Constantes de ruta
    private val CONFIG_FILE = "application.properties"
    private val APP_PATH: String = System.getProperty("user.dir")

    private val RESOURCE_PATH_MAIN =
        "$APP_PATH${File.separator}src${File.separator}main${File.separator}resources${File.separator}"


    // Utilizo la técnica "Backing Properties", mediante "getters" para encapsular nuestra configuración

    private var _dataFatherPath: String = "" // Ruta de la carpeta padre de los datos
    fun getDataFatherPath(): String {
        return _dataFatherPath
    }

    // Doy la posibilidad de asignar la extensión que queramos
    private var _dataOutputPath: String = "" // Ruta de la carpeta donde exportaremos datos
    fun getPathDataOutput(nameFile: String, extension: String): String {
        return _dataOutputPath + "${nameFile}.${extension}"
    }

    private var _dataInputPath: String = "" // Ruta de la carpeta donde importaremos datos
    fun getPathDataInput(nameFile: String, extension: String): String {
        return _dataInputPath + "${nameFile}.${extension}"
    }

    private var _dbUrlConection: String = "" // URL donde nos conectaremos a la base de datos
    fun getUrlDbConection(): String {
        return _dbUrlConection
    }

    private var _deleteDb: Boolean = false // Si permitimos borrar las tablas
    fun getDeleteDb(): Boolean {
        return _deleteDb
    }

    private var _isExcuteTest: Boolean = false // Si ejecutamos para Test
    fun getIsExcuteTest(): Boolean {
        return _isExcuteTest
    }

    // Probando imagenes
    /*private var _imagesDirectory: String = ""
    fun getImagesDirectory(): String {
        return _imagesDirectory
    }*/

    init {
        loadProperties()
        initStorage()
    }

    // Comprobamos si existe nuestra carpeta principal, donde almacenaremos nuestra gestión
    private fun initStorage() {
        // Creamos el directorio padre si no existe
        if (!Paths.get(_dataFatherPath).exists()) {
            logger.debug { "Creando carpeta padre de Datos" }
            Files.createDirectories(Paths.get(_dataFatherPath))
        }
    }

    // Cargamos siempre en primer lugar el fichero properties
    private fun loadProperties() {
        logger.debug { "Cargando properties (configuración)" }

        _dataFatherPath = "APP_PATH${readProperty("data.father", "data")}${File.separator}"

        // Recogemos la configuración del fichero, para ser llamadas desde los campos de ConfigApp
        // El segundo parámetro es por defecto! Por si no es capaz de leer el properties
        _dataOutputPath = _dataFatherPath + readProperty("data.output", "output") // Escritura
        _dataInputPath = _dataFatherPath + readProperty("data.input", "input") // Lectura

        _dbUrlConection = readProperty("db.url.connection", "jdbc:sqlite:database.db")

        _deleteDb = readProperty("db.delete", "false").toBoolean()

        //_imagesDirectory = readProperty("images", "") // Probando imagenes
    }

    private fun readProperty(propertie: String, defaultValuePropertie: String): String {
        logger.debug { "Leyendo propiedad: $propertie" }

        val properties = Properties()
        // Accedemos al fichero de propiedades mediante su nombre
        val propertiesFile = ClassLoader.getSystemResource(CONFIG_FILE).file
        // Cargamos el fichero a la clase Properties
        properties.load(FileInputStream(propertiesFile))

        return properties.getProperty(propertie, defaultValuePropertie)
    }
}