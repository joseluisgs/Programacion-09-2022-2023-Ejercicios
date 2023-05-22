package dev.kevin.vehiculosfx.services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.VehiculosQueries
import dev.kevin.database.AppDatabase
import dev.kevin.vehiculosfx.config.ConfigApp
import java.io.File
import java.nio.file.Files


class SqlDeLightClient(
    private val configApp: ConfigApp
) {
    val database: VehiculosQueries by lazy {

        JdbcSqliteDriver(configApp.APP_URL).let { driver ->
            configApp.logger.debug { "SqlDeLightClient.init() - Create Schemas" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.vehiculosQueries
    }

    init{
        configApp.logger.debug { "Se inicia el gestor de la base de datos" }

        if(!configApp.APP_INIT_DATABASE){
            configApp.logger.debug { "Se borra la base de datos" }
            Files.deleteIfExists(File(configApp.APP_URL.removePrefix("jdbc:sqlite:")).toPath())
        }

        if(configApp.APP_REMOVE_DATA_DATABASE){
            clearData()
        }
    }

    private fun clearData() {
        configApp.logger.debug { "Se borran los datos de la basae de datos" }
        database.transaction{
            database.deleteAll()
        }
    }

}