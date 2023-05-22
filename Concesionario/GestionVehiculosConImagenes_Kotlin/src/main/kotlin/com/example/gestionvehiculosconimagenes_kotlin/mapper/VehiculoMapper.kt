package com.example.gestionvehiculosconimagenes_kotlin.mapper

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.VehiculoReference
import database.VehiculoEntity
import javafx.scene.image.Image
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate

private val logger = KotlinLogging.logger {  }

fun VehiculoEntity.toVehiculo(): Vehiculo{
    logger.debug { "Transformamos un vehículoEntity en un vehículo" }
    return Vehiculo(
        id = this.id,
        marca = this.marca,
        modelo = this.modelo,
        tipoMotor = this.tipoMotor.getTipoMotor(),
        km = this.km,
        fechaMatriculacion = LocalDate.parse(this.fechaMatricula),
        foto = foto
    )
}

fun Vehiculo.toVehiculoReference(foto: Image, fotoFile: File): VehiculoReference{
    logger.debug { "Creamos un vehciulo refrence a partir de un vehiculo" }
    return VehiculoReference(
        id = id,
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
        marca = marca,
        modelo = modelo,
        tipoMotor = tipoMotor!!,
        km = km,
        fechaMatriculacion = fechaMatriculacion!!,
        foto = foto.url ?: ""
    )
}

fun String.getTipoMotor(): TipoMotor{
    logger.debug { "Conseguimos el tipo del coche según el texto: $this" }
    return when(this){
        "ELECTRICO" -> TipoMotor.ELECTRICO
        "GASOLINA" -> TipoMotor.GASOLINA
        "DIESEL" -> TipoMotor.DIESEL
        "HIBRIDO" -> TipoMotor.HIBRIDO
        "OTRO" -> TipoMotor.OTRO
        else -> TipoMotor.CUALQUIERA
    }
}