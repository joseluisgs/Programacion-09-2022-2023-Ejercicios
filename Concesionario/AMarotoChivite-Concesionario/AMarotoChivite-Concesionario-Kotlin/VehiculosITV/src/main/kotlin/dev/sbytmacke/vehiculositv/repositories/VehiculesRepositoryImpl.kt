package dev.sbytmacke.vehiculositv.repositories

import dev.sbytmacke.vehiculositv.mappers.toVehiculeOfSQDelight
import dev.sbytmacke.vehiculositv.models.Vehicule
import dev.sbytmacke.vehiculositv.services.database.VehiculeSqlDelightClient
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class VehiculesRepositoryImpl(
    private val databaseClient: VehiculeSqlDelightClient,
) : VehiculesRepository {

    // Cargamos el cliente
    private val database = databaseClient.vehiculeQueries

    override fun findAll(): List<Vehicule> {
        logger.debug { "findAll" }

        return database.selectAll().executeAsList().map { toVehiculeOfSQDelight(it) }
    }

    override fun save(item: Vehicule): Vehicule {
        logger.debug { "save: $item" }
        return if (!existItem(item)) {
            create(item)
        } else {
            update(item)
        }
    }

    private fun create(item: Vehicule): Vehicule {
        logger.debug { "create: $item" }
        // Insertamos y recuperamos el ID, transacción por funcion de sqlite (mira el .sq)
        database.transaction {
            database.insert(
                item.matricule,
                item.marca,
                item.modelo,
                item.typeMotor.toString(),
                item.km,
                item.fechaMantenimiento.toString()
            )
        }
        return toVehiculeOfSQDelight(database.selectLastInserted().executeAsOne())
    }

    private fun update(item: Vehicule): Vehicule {
        logger.debug { "update: $item" }
        database.update(
            item.matricule,
            item.marca,
            item.modelo,
            item.typeMotor.toString(),
            item.km,
            item.fechaMantenimiento.toString(),
            item.id
        )
        return item
    }

    override fun deleteById(id: Long) {
        logger.debug { "deleteById: $id" }
        return database.delete(id)
    }

    override fun deleteAll() {
        logger.debug { "deleteAll" }
        return database.deleteAll()
    }

    override fun saveAll(items: List<Vehicule>): List<Vehicule> {
        logger.debug { "saveAll: $items" }
        return items.map { save(it) }
    }

    override fun getByMatricule(matricule: String): Vehicule? {
        val result = database.getByMatricule(matricule).executeAsOneOrNull()

        return if (result != null) {
            toVehiculeOfSQDelight(result)
        } else {
            null
        }
    }

    override fun getById(id: Long): Vehicule? {
        logger.debug { "getById" }

        val result = database.getById(id).executeAsOneOrNull()

        return if (result != null) {
            toVehiculeOfSQDelight(result)
        } else {
            null
        }
    }

    override fun existItem(item: Vehicule): Boolean {
        logger.debug { "existItem" }

        return getById(item.id) != null
    }
}