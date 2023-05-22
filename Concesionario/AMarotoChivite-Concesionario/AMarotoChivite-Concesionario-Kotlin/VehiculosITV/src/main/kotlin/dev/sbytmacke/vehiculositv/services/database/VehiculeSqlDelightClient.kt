package dev.sbytmacke.vehiculositv.services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.VehiculeQueries
import dev.sbytmacke.vehiculositv.config.AppConfig
import dev.sbytmacke.vehiculositv.database.AppDatabase


private val logger = mu.KotlinLogging.logger { }

class VehiculeSqlDelightClient(private val appConfig: AppConfig) {

    lateinit var vehiculeQueries: VehiculeQueries
    private val connectionUrl = appConfig.getUrlDbConection()

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos" }

        initConfig()

        // Borramos la base de datos para no duplicar datos, si está en la config=true
        if (appConfig.getDeleteDb()) {
            removeAllData()
        }

        // Añadimos datos bases por defecto (INSERT)
        insertAllData()
    }

    private fun initConfig() {
        val driver = JdbcSqliteDriver(connectionUrl)
        logger.debug { "Creando Base de Datos" }
        AppDatabase.Schema.create(driver)

        val database = AppDatabase(driver)
        vehiculeQueries = database.vehiculeQueries
    }

    private fun insertAllData() {
        logger.debug { "Añadiendo todos los datos" }

        vehiculeQueries.transaction {
            vehiculeQueries.insertAll()
        }
    }

    private fun removeAllData() {
        logger.debug { "Eliminando todos los datos" }

        vehiculeQueries.transaction {
            vehiculeQueries.deleteAll()
        }
    }
}