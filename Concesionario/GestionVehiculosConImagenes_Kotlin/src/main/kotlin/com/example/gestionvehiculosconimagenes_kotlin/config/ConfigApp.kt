package com.example.gestionvehiculosconimagenes_kotlin.config

import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException
import java.lang.ClassLoader.getSystemResourceAsStream
import java.util.Properties

private  val logger = KotlinLogging.logger {  }

class ConfigApp {

    val APP_URL by lazy {
        //readProperty("app.database.url") ?: "jdbc:sqlite:Database.sq"
        readProperty("app.database.url") ?: "jdbc:mariadb://localhost:3306/empresacehiculo?serverTimezone=UTC"
    }

    val APP_USER: String by lazy {
        readProperty("app.database.user") ?: "root"
    }

    val APP_PASSWORD: String by lazy {
        readProperty("app.database.password") ?: ""
    }

    val APP_INIT: Boolean by lazy {
        (readProperty("app.database.init") ?: "true") == "true"
    }

    val APP_DELETE_ALL_BD: Boolean by lazy {
        (readProperty("app.database.deleteData") ?: "false") == "true"
    }

    val APP_IMAGES_PATH: String by lazy {
        System.getProperty("user.dir")+ File.separator+(readProperty("app.images.directory") ?: "images")
    }

    val APP_DELETE_ALL_IMAGES: Boolean by lazy {
        (readProperty("app.images.deleteData") ?: "false") == "true"
    }

    private fun readProperty(property: String): String?{
        logger.debug { "Cargamos la propiedad $property" }
        val properties = Properties()
        properties.load(getSystemResourceAsStream("application.properties") ?: throw FileNotFoundException("El fichero de propiedades no se ha encontrado."))
        return properties.getProperty(property)
    }
}