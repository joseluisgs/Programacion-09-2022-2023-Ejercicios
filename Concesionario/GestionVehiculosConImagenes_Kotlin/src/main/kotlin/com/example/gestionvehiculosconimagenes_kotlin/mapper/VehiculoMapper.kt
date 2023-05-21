package com.example.gestionvehiculosconimagenes_kotlin.mapper

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.VehiculoReference
import database.VehiculoEntity
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.lang.Exception
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

fun VehiculoEntity.toVehiculo(): Vehiculo{
    logger.debug { "Transformamos un vehículoEntity en un vehículo" }
    return Vehiculo(
        id = id,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        tipoMotor = tipoMotor.getTipoMotor(),
        km = km,
        fechaMatriculacion = fechaMatricula.toLocalDate(),
        foto = foto
    )
}

fun Vehiculo.toVehiculoReference(foto: Image, fotoFile: File): VehiculoReference{
    logger.debug { "Creamos un vehciulo refrence a partir de un vehiculo" }
    return VehiculoReference(
        id = id,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        tipoMotor = tipoMotor,
        km = km,
        fechaMatriculacion = fechaMatriculacion,
        foto = foto,
        fotoFile = fotoFile
    )
}

fun VehiculoReference.toVehiculo(): Vehiculo{
    logger.debug { "Creamos un vehiculo a partir de un vehiculo reference" }
    return Vehiculo(
        id = id,
        matricula = matricula,
        marca = marca,
        modelo = modelo,
        tipoMotor = tipoMotor!!,
        km = km,
        fechaMatriculacion = fechaMatriculacion!!,
        foto = foto.url ?: ""
    )
}

fun String.toLocalDate(): LocalDate{
    logger.debug { "Parseamos la fecha al tipo LocalDate" }
    var fecha = LocalDate.now()
    try {
        fecha = LocalDate.parse(this)
    }catch (_: Exception){
        var datos = this.split("/")
        if(datos.size == 3){
            try {
                fecha = LocalDate.of(datos[0].toInt(), datos[1].toInt(), datos[2].toInt())
            }catch (_: Exception){
                fecha = LocalDate.of(datos[2].toInt(), datos[1].toInt(), datos[0].toInt())
            }
        }
    }
    return fecha
}

fun String.getTipoMotor(): TipoMotor{
    logger.debug { "Conseguimos el tipo del motor según el texto: $this" }
    return when(this.uppercase()){
        "ELECTRICO" -> TipoMotor.ELECTRICO
        "GASOLINA" -> TipoMotor.GASOLINA
        "DIESEL" -> TipoMotor.DIESEL
        "HIBRIDO" -> TipoMotor.HIBRIDO
        "OTRO" -> TipoMotor.OTRO
        else -> TipoMotor.CUALQUIERA
    }
}