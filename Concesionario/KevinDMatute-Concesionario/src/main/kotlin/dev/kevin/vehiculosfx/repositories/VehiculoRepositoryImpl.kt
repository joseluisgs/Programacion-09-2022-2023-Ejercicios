package dev.kevin.vehiculosfx.repositories

import dev.kevin.vehiculosfx.mappers.toModel
import dev.kevin.vehiculosfx.models.Vehiculo
import dev.kevin.vehiculosfx.services.database.SqlDeLightClient
import mu.KotlinLogging

val logger = KotlinLogging.logger {  }

class VehiculoRepositoryImpl(
    private val databaseClient: SqlDeLightClient
): VehiculoRepository {

    val base = databaseClient.database

    override fun findAll(): List<Vehiculo> {
        return base.selectAll().executeAsList().map { it.toModel() }

    }

    override fun findById(id: Long): Vehiculo? {
        logger.debug { "findById: $id" }
        return base.selectById(id).executeAsOneOrNull()?.toModel()
    }

    override fun save(vehiculo: Vehiculo): Vehiculo {
        logger.debug { "save: $vehiculo" }
        return if(vehiculo.isNewVehiculo()){
            create(vehiculo)
        }else{
            update(vehiculo)
        }
    }

    private fun create(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "create: $vehiculo"}
        base.transaction {
            base.insert(
                marca = vehiculo.marca,
                matricula = vehiculo.matricula,
                modelo = vehiculo.modelo,
                motor = vehiculo.motor.toString(),
                km = vehiculo.km.toLong(),
                fechaMatriculacion = vehiculo.fechaMatriculacion.toString(),
                imagen = vehiculo.imagen
            )
        }
        return base.selectLastInserted().executeAsOne().toModel()
    }

    private fun update(vehiculo: Vehiculo): Vehiculo{
        logger.debug { "create: $vehiculo"}
        base.transaction {
            base.update(
                id = vehiculo.id,
                marca = vehiculo.marca,
                matricula = vehiculo.matricula,
                modelo = vehiculo.modelo,
                motor = vehiculo.motor.toString(),
                km = vehiculo.km.toLong(),
                fechaMatriculacion = vehiculo.fechaMatriculacion.toString(),
                imagen = vehiculo.imagen
            )
        }
        return vehiculo
    }

    override fun deleteById(id: Long){
        return base.delete(id)
    }

    override fun deleteAll() {
        logger.debug { "deleteAll" }
        return base.deleteAll()
    }


}