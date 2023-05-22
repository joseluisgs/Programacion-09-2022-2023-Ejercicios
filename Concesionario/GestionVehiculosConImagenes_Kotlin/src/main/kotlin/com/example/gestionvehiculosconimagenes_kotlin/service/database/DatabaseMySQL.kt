package com.example.gestionvehiculosconimagenes_kotlin.service.database

import com.example.gestionvehiculosconimagenes_kotlin.config.ConfigApp
import com.example.gestionvehiculosconimagenes_kotlin.mapper.getTipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.mapper.toLocalDate
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import mu.KotlinLogging
import java.sql.DriverManager
import java.sql.Statement

private val logger = KotlinLogging.logger {  }

class DatabaseMySQL(
    private val config: ConfigApp
) {
    val connection get() = DriverManager.getConnection(config.APP_URL, config.APP_USER, config.APP_PASSWORD)

    fun selectAllVehiculos(): List<Vehiculo> {
        logger.debug { "Tomamos todos los vehículos de la BBDD" }
        val vehiculos = mutableListOf<Vehiculo>()
        connection.use {
            val sql = "SELECT * FROM vehiculo;"

            it.prepareStatement(sql).use { stm ->
                val result = stm.executeQuery()
                while(result.next()){
                    vehiculos.add(
                        Vehiculo(
                            id = result.getLong("id_vehiculo"),
                            matricula = result.getString("matricula"),
                            marca = result.getString("marca"),
                            modelo = result.getString("modelo"),
                            tipoMotor = result.getString("tipo_motor").getTipoMotor(),
                            km = result.getDouble("kilometros"),
                            fechaMatriculacion = result.getString("fecha_matriculacion").toLocalDate(),
                            foto = result.getString("foto")
                        )
                    )
                }
            }
        }
        return vehiculos
    }

    fun selectVehiculoById(id: Long): Vehiculo? {
        logger.debug { "Tomamos un vehículos de la BBDD, el de id $id" }
        var vehiculo: Vehiculo? = null
        connection.use {
            val sql = "SELECT * FROM vehiculo WHERE id_vehiculo = ?;"

            it.prepareStatement(sql).use { stm ->

                stm.setLong(1, id)

                val result = stm.executeQuery()
                while (result.next()) {
                    vehiculo =
                        Vehiculo(
                            id = result.getLong("id_vehiculo"),
                            matricula = result.getString("matricula"),
                            marca = result.getString("marca"),
                            modelo = result.getString("modelo"),
                            tipoMotor = result.getString("tipo_motor").getTipoMotor(),
                            km = result.getDouble("kilometros"),
                            fechaMatriculacion = result.getString("fecha_matriculacion").toLocalDate(),
                            foto = result.getString("foto")
                        )
                }
            }
        }
        return vehiculo
    }

    fun insertVehiculo(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "Se inserta un nuevo vehículo en la BBDD" }
        var myId = 0L
        connection.use {
            val sql = "INSERT INTO vehiculo VALUES (null, ?,?,?,?,?,?,?);"

            it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use{stm ->
                stm.setString(1, vehiculo.matricula)
                stm.setString(2, vehiculo.marca)
                stm.setString(3, vehiculo.modelo)
                stm.setString(4, vehiculo.tipoMotorText)
                stm.setDouble(5, vehiculo.km)
                stm.setString(6, vehiculo.fechaMatriculacion.toString())
                stm.setString(7, vehiculo.foto)

                stm.executeUpdate()

                val id = stm.generatedKeys
                while(id.next()){
                    myId = id.getLong(1)
                }
            }
        }
        return vehiculo.copy(id = myId)
    }

    fun updateVehiculo(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "Actualizo un vehículo, el de id ${vehiculo.id}" }
        connection.use {
            val sql = "UPDATE vehiculo SET marca = ?, modelo = ?, tipo_motor = ?, kilometros = ?, fecha_matriculacion = ?, foto = ? WHERE id_vehiculo = ?;"

            it.prepareStatement(sql).use{stm ->
                stm.setString(1, vehiculo.marca)
                stm.setString(2, vehiculo.modelo)
                stm.setString(3, vehiculo.tipoMotorText)
                stm.setDouble(4, vehiculo.km)
                stm.setString(5, vehiculo.fechaMatriculacion.toString())
                stm.setString(6, vehiculo.foto)
                stm.setLong(7, vehiculo.id)

                stm.executeUpdate()

            }
        }
        return vehiculo
    }

    fun deleteVehiculo(id: Long) {
        logger.debug { "Borramos al vehículo de id $id" }
        connection.use {
            val sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?;"
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, id)

                stm.executeUpdate()
            }
        }
    }

    fun deleteAllVehiculos() {
        logger.debug { "Borramos todos los vehículos" }
        connection.use {
            val sql = "DELETE FROM vehiculo;"
            it.prepareStatement(sql).use { stm ->
                stm.executeUpdate()
            }
        }
    }

}