package com.example.concesionario.repositories.vehicle

import com.example.concesionario.errors.VehicleError
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import repositories.CrudRepository

interface VehicleRepository: CrudRepository<Vehicle, Long, VehicleError> {
    fun findVehicleGroupByMotor(): Map<Motor, List<Vehicle>>
}