package com.example.gestionvehiculosconimagenes_kotlin.validator

import com.example.gestionvehiculosconimagenes_kotlin.error.VehiculoError
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

fun Vehiculo.validate(): Result<Vehiculo, VehiculoError>{
    logger.debug { "Se válidan los datos del vehículo" }
    require(marca.isNotEmpty()){
        return Err(VehiculoError.MarcaNoValida(marca))
    }
    require(modelo.isNotEmpty()){
        return Err(VehiculoError.ModeloNoValido(modelo))
    }
    require(marca.isNotEmpty()){
        return Err(VehiculoError.MarcaNoValida(marca))
    }
    require(km >= 0.0){
        return Err(VehiculoError.KilometroNoValido(km.toString()))
    }

    require(fechaMatriculacion.isBefore(LocalDate.now())){
        return Err(VehiculoError.FechaMatriculacionNoValida(fechaMatriculacion))
    }
    return Ok(this)
}
