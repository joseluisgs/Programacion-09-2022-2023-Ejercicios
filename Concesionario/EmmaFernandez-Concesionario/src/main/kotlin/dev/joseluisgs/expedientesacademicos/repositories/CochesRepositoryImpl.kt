package dev.joseluisgs.expedientesacademicos.repositories

import dev.joseluisgs.expedientesacademicos.mappers.toModel
import dev.joseluisgs.expedientesacademicos.models.Coche
import dev.joseluisgs.expedientesacademicos.services.database.SqlDeLightClient
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class CochesRepositoryImpl(
    private val databaseClient: SqlDeLightClient
) : CochesRepository {

    val database = databaseClient.database


    override fun findAll(): List<Coche> {
        logger.debug { "findAll" }
        return database.selectAll().executeAsList().map { it.toModel() }
    }

    override fun findById(id: Long): Coche? {
        logger.debug { "findById: $id" }

        return database.selectById(id).executeAsOneOrNull()?.toModel()
    }

    override fun save(coche: Coche): Coche {
        logger.debug { "save: $coche" }
        return if (coche.isNewCoche()) {
            create(coche)
        } else {
            update(coche)
        }
    }

    private fun create(coche: Coche): Coche {
        logger.debug { "create: $coche" }
        // Insertamos y recuperamos el ID, transacción por funcion de sqlite (mira el .sq)
        database.transaction {
            database.insert(
                matricula = coche.matricula,
                marca = coche.marca,
                modelo = coche.modelo,
                tipoMotor = coche.tipoMotor.name,
                fechaMatriculacion = coche.fechaMatriculacion.toString(),
                imagen = coche.imagen
            )
        }
        return database.selectLastInserted().executeAsOne().toModel()
    }

    private fun update(coche: Coche): Coche {
        logger.debug { "update: $coche" }
        database.update(
            id = coche.id,
            matricula = coche.matricula,
            marca = coche.marca,
            modelo = coche.modelo,
            tipoMotor = coche.tipoMotor.name,
            fechaMatriculacion = coche.fechaMatriculacion.toString(),
            imagen = coche.imagen
        )
        return coche
    }

    override fun deleteById(id: Long) {
        logger.debug { "deleteById: $id" }
        return database.delete(id)
    }

    override fun deleteAll() {
        logger.debug { "deleteAll" }
        return database.deleteAll()
    }

    override fun saveAll(alumnos: List<Coche>): List<Coche> {
        logger.debug { "saveAll: $alumnos" }
        return alumnos.map { save(it) }
    }
}
