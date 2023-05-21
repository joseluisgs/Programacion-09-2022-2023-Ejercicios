package dev.kevin.vehiculosfx.config

import mu.KotlinLogging
import java.io.File
import java.util.*


class ConfigApp {

    val logger = KotlinLogging.logger {  }

    val APP_PATH = System.getProperty("user.dir")

    val APP_IMAGES by lazy {
        val path = readProperty("APP_IMAGES") ?: "images"
        "$APP_PATH${File.separator}$path/images"
    }

    val APP_NAME by lazy {
        readProperty("APP_NAME") ?: "vehiculoFX"
    }

    val APP_AUTHOR by lazy {
        readProperty("APP_AUTHOR") ?: "KevinMatute"
    }

    val APP_DATA by lazy {
        readProperty("APP_DATA") ?: "data"
    }

    val APP_URL by lazy {
        readProperty("APP_URL") ?: "databse.db"
    }

    val APP_INIT_DATABASE: Boolean by lazy {
        readProperty("APP_INIT_DATABASE")?.toBoolean() ?: true
    }

    val APP_REMOVE_DATA_DATABASE: Boolean by lazy {
        readProperty("APP_REMOVE_DATA_DATABASE")?.toBoolean() ?: true
    }

    init {
        logger.debug { "Se inicia la configuracion de la aplicacion" }
    }

    private fun readProperty(property: String): String? {
        logger.debug { "Se leen las propiedades del config.properties" }

        val properties = Properties()
        properties.load(ConfigApp::class.java.getResourceAsStream("/config.properties"))
        return properties.getProperty(property)
    }

}