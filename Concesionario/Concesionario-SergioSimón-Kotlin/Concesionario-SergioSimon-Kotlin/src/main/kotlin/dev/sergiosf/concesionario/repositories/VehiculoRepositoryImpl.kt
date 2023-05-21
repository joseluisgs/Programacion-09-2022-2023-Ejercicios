package dev.sergiosf.concesionario.repositories

import dev.sergiosf.concesionario.models.Vehiculo
import dev.sergiosf.concesionario.service.database.DataBaseService
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.time.LocalDate


private val logger = KotlinLogging.logger {}

class VehiculoRepositoryImpl(
        private val dataBaseService: DataBaseService
):  VehiculoRepository, KoinComponent {

    override fun findAll(): List<Vehiculo> {
        logger.debug { "Cargando los vehículos desde una base de datos" }

        val items = mutableListOf<Vehiculo>()

        val sql = "SELECT * FROM vehiculos"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    items.add(
                        Vehiculo(
                            id = rs.getLong("id"),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            matricula = rs.getString("matricula"),
                            tipoVehiculo = toTipoVehiculo(rs.getString("tipoVehiculo")),
                            fechaMatriculation = LocalDate.parse(rs.getString("fechaMatriculation")),
                            image =rs.getString("image"),
                        )
                    )
                }
            }
        }
        return items
    }

    private fun toTipoVehiculo(inPut: String?): Vehiculo.TipoVehiculo {
        return when (inPut) {
            "GASOLINA" -> Vehiculo.TipoVehiculo.GASOLINA
            "DIESEL" -> Vehiculo.TipoVehiculo.DIESEL
            "ELECTRICO" -> Vehiculo.TipoVehiculo.ELECTRICO
            "HIBRIDO" -> Vehiculo.TipoVehiculo.HIBRIDO
            else -> {
                Vehiculo.TipoVehiculo.NONE
            }
        }
    }

    override fun findById(id: Long): Vehiculo? {
        logger.debug { "Buscando vehículo con el id: $id" }
        var item: Vehiculo? = null
        val sql = "SELECT * FROM vehiculos WHERE id =?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, id)
                val rs = stm.executeQuery()
                rs?.let {
                    while (it.next()) {
                        item = Vehiculo(
                            id = rs.getLong("id"),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            matricula = rs.getString("matricula"),
                            tipoVehiculo = toTipoVehiculo(rs.getString("tipoVehiculo")),
                            fechaMatriculation = LocalDate.parse(rs.getString("fechaMatriculation")),
                            image =rs.getString("image"),
                        )
                    }
                }
            }
        }
        return item
    }

    override fun findByMatricula(matricula: String): Vehiculo? {
        logger.debug { "Buscando vehículo con la matricula: $matricula" }
        var item: Vehiculo? = null
        val sql = "SELECT * FROM vehiculos WHERE matricula =?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, matricula)
                val rs = stm.executeQuery()
                rs?.let {
                    while (it.next()) {
                        item = Vehiculo(
                                id = rs.getLong("id"),
                                marca = rs.getString("marca"),
                                modelo = rs.getString("modelo"),
                                matricula = rs.getString("matricula"),
                                tipoVehiculo = toTipoVehiculo(rs.getString("tipoVehiculo")),
                                fechaMatriculation = LocalDate.parse(rs.getString("fechaMatriculation")),
                                image =rs.getString("image"),
                        )
                    }
                }
            }
        }
        return item
    }

    override fun save(entity: Vehiculo): Vehiculo {
        logger.debug { "Guardando vehículo en la base de datos" }

        // Revisar si existe el vehículo para no crear uno nuevo
        return if (entity.isNewVehiculo()){
            create(entity)
        } else {
            update(entity)
        }
    }

    private fun update(entity: Vehiculo): Vehiculo {
        logger.debug { "Actualizando vehículo en la base de datos" }
        logger.debug { entity.id }
        val sql = """
        UPDATE vehiculos SET marca =?, modelo =?, matricula =?, tipoVehiculo =?, fechaMatriculation =?, image =? WHERE id =?
        """.trimIndent()
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, entity.marca)
                stm.setString(2, entity.modelo)
                stm.setString(3, entity.matricula)
                stm.setString(4, entity.tipoVehiculo.toString())
                stm.setString(5, entity.fechaMatriculation.toString())
                stm.setString(6, entity.image)
                stm.setLong(7, entity.id)
            }
        }
        return entity
    }

    private fun create(entity: Vehiculo): Vehiculo {
        logger.debug { "Creando vehículo en la base de datos" }
        var newId: Long = 0

        val sql = """
            INSERT INTO vehiculos VALUES (NULL,?,?,?,?,?,?)
        """.trimIndent()

        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, entity.marca)
                stm.setString(2, entity.modelo)
                stm.setString(3, entity.matricula)
                stm.setString(4, entity.tipoVehiculo.toString())
                stm.setString(5, entity.fechaMatriculation.toString())
                stm.setString(6, entity.image)

                stm.executeUpdate()

                val claves = stm.generatedKeys
                if(claves.next()){
                    newId = claves.getLong(1)
                }
            }
        }

        return entity.copy(id = newId)
    }

    override fun deleteById(id: Long) {
        logger.debug { "Borrando vehículos con id $id" }

        val sql = """
            DELETE FROM vehiculos WHERE id =?
        """.trimIndent()
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, id)

                stm.executeUpdate()
            }
        }
    }

    override fun deleteAll() {
        logger.debug("Borrando todas los vehículos")

        val sql = """
            DELETE FROM vehiculos
        """.trimIndent()

        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.executeUpdate()
            }
        }
    }

    override fun saveAll(vehiculos: List<Vehiculo>): List<Vehiculo> {
        logger.debug { "Guardando todas los vehiculos" }

        vehiculos.forEach{
            save(it)
        }
        return vehiculos
    }
}