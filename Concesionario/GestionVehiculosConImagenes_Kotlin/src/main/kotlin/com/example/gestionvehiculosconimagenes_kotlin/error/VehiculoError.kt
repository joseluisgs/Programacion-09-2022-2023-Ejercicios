package com.example.gestionvehiculosconimagenes_kotlin.error

import java.time.LocalDate

sealed class VehiculoError(val message: String){
    class MarcaNoValida(message: String): VehiculoError("La marca '$message', no es válida.")
    class ModeloNoValido(message: String): VehiculoError("El modelo '$message', no es válido.")
    class KilometroNoValido(message: String): VehiculoError("Los kilometros '$message', no son válidos.")
    class FechaMatriculacionNoValida(message: LocalDate): VehiculoError("La fecha de matriculación '$message', no es válida.")
    class SameDateUpdate(message: Long): VehiculoError("No has cambiado ningún dato al editar el vehículo de id: $message")
    class ImageNotFound(message: String): VehiculoError("La imagen '$message', no se ha encontrado.")
    class SaveImage(message: String): VehiculoError("Ha ocurrido un error al guardar la nueva imagen: $message")
}
