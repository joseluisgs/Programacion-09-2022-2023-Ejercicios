package com.example.concesionario.mappers

import com.example.concesionario.dto.VehicleDto
import com.example.concesionario.models.Motor
import com.example.concesionario.models.Vehicle
import java.time.LocalDate

fun Vehicle.toDto(): VehicleDto = VehicleDto(
    id.toString(),
    marca,
    modelo,
    motor.toString(),
    fehcaMatriculacion.toString(),
    imagenUrl
)

fun VehicleDto.toClass(): Vehicle = Vehicle(
    id.toLong(),
    marca,
    modelo,
    Motor.valueOf(motor),
    LocalDate.parse(fehcaMatriculacion),
    imagenUrl
)