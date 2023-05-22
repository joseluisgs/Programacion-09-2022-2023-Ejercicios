package dev.sergiosf.concesionario.service.database

import dev.sergiosf.concesionario.config.AppConfig
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.sql.DriverManager

private val logger = KotlinLogging.logger {}


class DataBaseService(
        private val appConfig: AppConfig
): KoinComponent {

    val db
        get() = DriverManager.getConnection(appConfig.databaseUrl)

    init {
        logger.debug { "Iniciando DataBaseService" }
        if (appConfig.databaseDropTable){
            dropTables()
        }
        createTables()
    }

    private fun dropTables() {
        val sql = "DROP TABLE IF EXISTS vehiculos"
        logger.debug { "Drop vehículos table" }
        db.use {
            it.createStatement().use { stmt ->
                stmt.executeUpdate(sql)
            }
        }
    }


    private fun createTables() {
        val sql = """
            CREATE TABLE IF NOT EXISTS vehiculos (
                id  INTEGER PRIMARY KEY AUTOINCREMENT,
                marca TEXT NOT NULL,
                modelo TEXT NOT NULL,
                matricula TEXT NOT NULL,
                tipoVehiculo TEXT NOT NULL,
                fechaMatriculation TEXT NOT NULL,
                image TEXT NOT NULL
            )
            """.trimIndent()

        logger.debug { "Creando tabla vehículos" }
        db.use {
            it.createStatement().use { stmt ->
                stmt.executeUpdate(sql)
            }
        }
    }
}