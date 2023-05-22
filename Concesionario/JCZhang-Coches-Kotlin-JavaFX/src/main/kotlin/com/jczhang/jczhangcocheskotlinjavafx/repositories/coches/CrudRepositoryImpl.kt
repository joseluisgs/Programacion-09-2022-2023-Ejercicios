package com.jczhang.jczhangcocheskotlinjavafx.repositories.coches

import com.jczhang.jczhangcocheskotlinjavafx.models.Coche
import com.jczhang.jczhangcocheskotlinjavafx.repositories.base.CrudRepository
import com.jczhang.jczhangcocheskotlinjavafx.services.database.DatabaseService
import com.jczhang.jczhangcocheskotlinjavafx.utils.localDateFromString
import mu.KotlinLogging

class CrudRepositoryImpl(private val databaseService: DatabaseService) : CrudRepository<Coche> {

    private val logger = KotlinLogging.logger { }

    override fun addToDataBase(item: Coche) {
        logger.debug { "adding into database" }
        val sql = """INSERT INTO coches values(null, ?,?,?,?,?)""".trimIndent()

        databaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, item.marca)
                stm.setString(2, item.modelo)
                stm.setString(3, item.tipoMotor.uppercase())
                stm.setDouble(4, item.km)
                stm.setString(5, item.matriculacion.toString())

                stm.executeUpdate()
            }
        }
    }

    override fun loadFromDataBase(): List<Coche> {
        logger.debug { "Loading coches" }
        val sql = """SELECT * from coches""".trimIndent()
        val items = mutableListOf<Coche>()
        databaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    items.add(
                        Coche(
                            id = rs.getLong("id"),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            tipoMotor = rs.getString("tipo"),
                            km = rs.getDouble("km"),
                            matriculacion = localDateFromString(rs.getString("matriculacion"))
                        )
                    )
                }
            }
        }
        return items
    }


    override fun updateItem(id: Long?, marca: String, modelo: String, tipo: String, km: Double, fecha: String) {
        val sql =
            """UPDATE coches SET marca = ?, modelo = ?, tipo = ?, km = ?, matriculacion = ? WHERE id = ?""".trimIndent()

        databaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, marca)
                stm.setString(2, modelo)
                stm.setString(3, tipo)
                stm.setDouble(4, km)
                stm.setString(5, fecha)
                stm.setLong(6, id!!)

                stm.executeUpdate()
            }
        }
    }


    override fun deleteFromDatabase(idBuscado: Long?) {
        logger.debug { "Deleting from database" }

        val sql = """DELETE FROM coches where id = $idBuscado """.trimIndent()

        databaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.executeUpdate()
            }
        }
    }
}