package com.example.vehiculoscrudinterface.mappers

import com.example.vehiculoscrudinterface.dto.VehiculoDto
import com.example.vehiculoscrudinterface.enums.TipoMotor
import com.example.vehiculoscrudinterface.models.Vehiculo
import java.time.LocalDate

fun Vehiculo.toVehiculoDto() = VehiculoDto(
    this.id,
    this.marca,
    this.modelo,
    this.tipoMotor.toString(),
    this.km.toString(),
    this.fechaMatriculacion.toString(),
    this.imagen
)

fun VehiculoDto.toVehiculo() = Vehiculo(
    this.id,
    this.marca,
    this.modelo,
    TipoMotor.valueOf(this.tipoMotor),
    this.km.toInt(),
    LocalDate.parse(this.fechaMatriculacion),
    this.imagen
)

fun List<Vehiculo>.toVehiculoListDto() = this.map { it.toVehiculoDto() }

fun List<VehiculoDto>.toVehiculoList() = this.map { it.toVehiculo() }