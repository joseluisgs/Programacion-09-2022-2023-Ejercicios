package com.example.concesionario.config

import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = KotlinLogging.logger {}

private const val CONFIG_FILE_NAME = "/config.properties"

class AppConfig {
    private val localPath = System.getProperty("user.dir")

    val appData by lazy {
        localPath + File.separator + (readProperty("app.storage.dir") ?: "jdbc:sqlite:database.db")
    }

    val appDBURL by lazy {
        readProperty("app.db.url") ?: "jdbc:sqlite:database.db"
    }

    val appDBReset by lazy {
        readProperty("app.db.reset")?.toBoolean() ?: false
    }

    val appPathResources by lazy {
        localPath + File.separator + ((readProperty("app.path.resources") ?: "src-main-resources-").replace("-", File.separator))
    }

    val appDBInitPath by lazy {
        appPathResources + (readProperty("app.db.init.path") ?: "init.sql")
    }

    val appDBResetPath by lazy {
        appPathResources + (readProperty("app.db.reset.path") ?: "reset.sql")
    }

    init {
        initStorage()
    }

    private fun initStorage() {
        logger.debug { "Creando directorio ${appData} si no existe" }
        Files.createDirectories(Paths.get(appData))
    }

    private fun readProperty(propiedad: String): String? {
        logger.debug { "Leyendo propiedad: $propiedad" }
        val properties = Properties()
        properties.load(AppConfig::class.java.getResourceAsStream(CONFIG_FILE_NAME))
        return properties.getProperty(propiedad)
    }
}
