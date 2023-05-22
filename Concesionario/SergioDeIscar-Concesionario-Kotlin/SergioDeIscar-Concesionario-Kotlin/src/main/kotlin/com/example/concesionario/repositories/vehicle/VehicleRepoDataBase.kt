package com.example.concesionario.repositories.vehicle

import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.errors.VehicleError
import com.example.concesionario.mappers.toClass
import com.example.concesionario.mappers.toDto
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import com.example.concesionario.services.database.DataBaseManager
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Statement

class VehicleRepoDataBase: VehicleRepository, KoinComponent {
    private val dataBaseManager:DataBaseManager by inject()

    override fun findVehicleGroupByMotor(): Map<Motor, List<Vehicle>> {
        return findAll().groupBy { it.motor }
    }

    override fun findAll(): Iterable<Vehicle> {
        val sql = """SELECT * FROM tVehicle"""
        val vehicles = mutableListOf<Vehicle>()
        dataBaseManager.dataBase.prepareStatement(sql).use { stm ->
            val result = stm.executeQuery()
            while (result.next()){
                vehicles.add(
                    VehicleDto(
                        result.getInt("nIdVehicle").toString(),
                        result.getString("cMarca"),
                        result.getString("cModelo"),
                        result.getString("cMotor"),
                        result.getString("dMatriculacion"),
                        null,
                    ).toClass()
                )
            }
        }
        return vehicles.toList()
    }

    override fun findById(id: Long): Result<Vehicle, VehicleError>{
        val sql = """SELECT * FROM tVehicle WHERE nIdVehicle = ?"""
        var vehicle: Vehicle? = null
        dataBaseManager.dataBase.prepareStatement(sql).use { stm ->
            stm.setLong(1, id)
            val result = stm.executeQuery()
            if (result.next()){
                vehicle = VehicleDto(
                    result.getInt("nIdVehicle").toString(),
                    result.getString("cMarca"),
                    result.getString("cModelo"),
                    result.getString("cMotor"),
                    result.getString("dMatriculacion"),
                    result.getString("cImageUrl"),
                ).toClass()
            }
        }
        return if(vehicle == null) Err(VehicleError.VehiculoNoEncontrado(id)) else Ok(vehicle!!)
    }

    override fun save(element: Vehicle): Result<Vehicle, VehicleError> {
        return if (existsById(element.id)){
            update(element)
        }else{
            create(element)
        }
    }

    private fun update(element: Vehicle): Result<Vehicle, VehicleError> {
        val dto = element.toDto()
        var result = 0
        val sql = """UPDATE tVehicle SET cMarca = ?, cModelo = ?, cMotor = ?, dMatriculacion = ?, cImageUrl = ? WHERE nIdVehicle = ?"""
        dataBaseManager.dataBase.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, dto.marca)
                stm.setString(2, dto.modelo)
                stm.setString(3, dto.motor)
                stm.setString(4, dto.fehcaMatriculacion)
                stm.setString(5, dto.imagenUrl)
                stm.setLong(6, dto.id.toLong())
                result = stm.executeUpdate()
            }
        }
        return if (result == 0) Err(VehicleError.VehiculoNoEncontrado(element.id)) else Ok(element)
    }

    private fun create(element: Vehicle): Result<Vehicle, VehicleError> {
        val dto = element.toDto()
        var newId = 0L
        val sql = """INSERT INTO tVehicle (cMarca, cModelo, cMotor, dMatriculacion, cImageUrl) VALUES (?, ?, ?, ?, ?)"""
        dataBaseManager.dataBase.use {
            it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stm ->
                stm.setString(1, dto.marca)
                stm.setString(2, dto.modelo)
                stm.setString(3, dto.motor)
                stm.setString(4, dto.fehcaMatriculacion)
                stm.setString(5, dto.imagenUrl)
                stm.executeUpdate()
                val result = stm.generatedKeys
                if (result.next()){
                    newId = result.getLong(1)
                }
            }
        }
        return if (newId == 0L) Err(VehicleError.VehiculoNoCreado(element.id)) else Ok(element.copy(id = newId))
    }

    override fun saveAll(elements: Iterable<Vehicle>) {
        elements.forEach { save(it) }
    }

    override fun deleteById(id: Long): Result<Boolean, VehicleError> {
        var result = 0
        val sql = """DELETE FROM tVehicle WHERE nIdVehicle = ?"""
        dataBaseManager.dataBase.use {
            it.prepareStatement(sql).use { stm ->
                stm.setLong(1, id)
                result = stm.executeUpdate()
            }
        }
        return if (result == 0) Err(VehicleError.VehiculoNoBorrado(id)) else Ok(true)
    }

    override fun delete(element: Vehicle): Result<Boolean, VehicleError> {
        return deleteById(element.id)
    }

    override fun deleteAll() {
        val sql = """DELETE FROM tVehicle"""
        dataBaseManager.dataBase.use {
            it.prepareStatement(sql).use { stm ->
                stm.executeUpdate()
            }
        }
    }

    override fun existsById(id: Long): Boolean {
        return findById(id).get() != null
    }
}