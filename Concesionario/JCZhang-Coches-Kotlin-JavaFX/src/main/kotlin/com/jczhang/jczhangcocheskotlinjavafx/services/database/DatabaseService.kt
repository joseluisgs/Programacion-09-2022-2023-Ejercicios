package com.jczhang.jczhangcocheskotlinjavafx.services.database

import com.jczhang.jczhangcocheskotlinjavafx.config.AppConfig
import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager

class DatabaseService(private val appConfig: AppConfig){

    private val logger = KotlinLogging.logger {}

    val db: Connection get() = DriverManager.getConnection(appConfig.databaseUrl)

    init {
        logger.debug { "Iniciando la base de datos" }
        if (appConfig.databaseDropTable){
            dropTable()
        }
        createTables()
    }

    private fun createTables() {
        val sql = """CREATE TABLE IF NOT EXISTS coches(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            marca TEXT NOT NULL,
            modelo TEXT NOT NULL,
            tipo TEXT NOT NULL,
            km REAL NOT NULL,
            matriculacion TEXT NOT NULL
            ) """.trimIndent()
        logger.debug { "Create tables" }

        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql)
            }
        }
    }


    private fun dropTable() {
        val sql = """DROP TABLE IF EXISTS coches """.trimIndent()


        logger.debug { "Drop tables" }
        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql)
            }
        }
    }
}