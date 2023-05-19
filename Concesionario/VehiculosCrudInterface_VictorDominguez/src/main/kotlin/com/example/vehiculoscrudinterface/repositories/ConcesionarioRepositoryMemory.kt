package com.example.vehiculoscrudinterface.repositories

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.enums.TipoMotor
import com.example.vehiculoscrudinterface.models.Vehiculo
import com.example.vehiculoscrudinterface.services.storage.database.DataBaseManager
import javafx.scene.image.Image
import mu.KotlinLogging
import java.time.LocalDate


private val logger = KotlinLogging.logger{}

class ConcesionarioRepositoryMemory(
    private val database: DataBaseManager = DataBaseManager
): ConcesionarioRepository {

    private val iconos = listOf<Image>(
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheAmarillo.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheAzul.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheAzulOscuro.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheBlanco.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheMorado.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheNegro.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheRojo.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheRosa.png")),
        Image(ConcesionarioApplication::class.java.getResourceAsStream("icons/cocheVerde.png"))
    )

    fun importar(vehiculos: List<Vehiculo>) {
        vehiculos.forEach { salvar(it) }
    }

    override fun buscarTodos(): List<Vehiculo> {
        logger.debug { "ConcesionarioRepository -> Buscar todos" }

        val vehiculos = mutableListOf<Vehiculo>()
        val sql = "SELECT * FROM TVehiculos"

        database.connection.prepareStatement(sql).use { stm ->
            val result = stm.executeQuery()

            while (result.next()) {
                vehiculos.add(
                    Vehiculo(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        TipoMotor.valueOf(result.getString(4)),
                        result.getInt(5),
                        LocalDate.parse(result.getString(6)),
                        result.getString(7)
                    )
                )
            }
        }
        return vehiculos.sortedBy { it.id.substring(4, 6) }
    }

    override fun buscarPorId(id: String): Vehiculo? {
        logger.debug { "ConcesionarioRepository -> Buscar por ID: $id" }

        val sql = "SELECT * FROM TVehiculos WHERE cId = ?"

        database.connection.prepareStatement(sql).use { stm ->
            stm.setString(1, id)
            val result = stm.executeQuery()
            if (result.next()) {
                return Vehiculo(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    TipoMotor.valueOf(result.getString(4)),
                    result.getInt(5),
                    LocalDate.parse(result.getString(6)),
                    result.getString(7)
                )
            }
        }
        return null
    }

    override fun eliminarTodos() {
        logger.debug { "ConcesionarioRepository -> Eliminar todos" }

        val sql = "DELETE FROM TVehiculos"

        database.connection.prepareStatement(sql).executeUpdate()
    }

    override fun eliminarPorId(id: String): Vehiculo? {
        logger.debug { "ConcesionarioRepository -> Eliminar por ID: $id" }

        val sql = "DELETE FROM TVehiculos WHERE cId = ?"
        val vehiculo = buscarPorId(id) ?: return null

        database.connection.prepareStatement(sql).use { stm ->
            stm.setString(1, id)
            if (stm.executeUpdate() == 1) { return vehiculo }
        }
        return null
    }

    override fun salvar(item: Vehiculo): Vehiculo {
        logger.debug { "ConcesionarioRepository -> Salvar vehiculo: $item" }
        return if (buscarPorId(item.id) == null) {
            guardar(item)
        } else actualizar(item)
    }

    private fun guardar(item: Vehiculo): Vehiculo {
        logger.debug { "ConcesionarioRepository -> Guardar vehículo: $item" }

        val sql = "INSERT INTO TVehiculos VALUES (?, ?, ?, ?, ?, ?, ?)"

        database.connection.prepareStatement(sql).use { stm ->
            stm.setString(1, item.id)
            stm.setString(2, item.marca)
            stm.setString(3, item.modelo)
            stm.setString(4, item.tipoMotor.toString())
            stm.setInt(5, item.km)
            stm.setString(6, item.fechaMatriculacion.toString())
            stm.setString(7, item.imagen)
            stm.executeUpdate()
        }
        return item
    }

    private fun actualizar(item: Vehiculo): Vehiculo {
        logger.debug { "ConcesionarioRepository -> Actualizar vehículo: $item" }

        val sql = "UPDATE TVehiculos SET cMarca = ?, cModelo = ?, cTipoMotor = ?, nKilometraje = ?, cMatriculacion = ?, cImagen = ? WHERE cId = ?"

        database.connection.prepareStatement(sql).use { stm ->
            stm.setString(1, item.marca)
            stm.setString(2, item.modelo)
            stm.setString(3, item.tipoMotor.toString())
            stm.setInt(4, item.km)
            stm.setString(5, item.fechaMatriculacion.toString())
            stm.setString(6, item.imagen)
            stm.setString(7, item.id)
            stm.executeUpdate()
        }
        return item
    }
}