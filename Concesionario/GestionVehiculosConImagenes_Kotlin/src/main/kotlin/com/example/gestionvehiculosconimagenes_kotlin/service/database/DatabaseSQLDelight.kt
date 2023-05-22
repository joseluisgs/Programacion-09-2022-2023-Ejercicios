package com.example.gestionvehiculosconimagenes_kotlin.service.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.gestionvehiculosconimagenes_kotlin.config.ConfigApp
import database.DatabaseQueries
import dev.ivanrc.database.AppDatabase
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val logger = KotlinLogging.logger {  }

class DatabaseSQLDelight(
    private val config: ConfigApp
) {
    val queries: DatabaseQueries by lazy {
        JdbcSqliteDriver(config.APP_URL).let { driver ->
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries
    }

    init{
        logger.debug { "Se inicia la database" }

        if(config.APP_INIT){
            Files.deleteIfExists(Path.of(config.APP_URL.removePrefix("jdbc:sqlite:")))
        }
        if(config.APP_DELETE_ALL_BD){
            queries.transaction {
                queries.deleteAllVehiculos()
            }
        }
    }
}