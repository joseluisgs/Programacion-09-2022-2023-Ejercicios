package com.example.gestionvehiculosconimagenes_kotlin.error

import java.time.LocalDate

sealed class VehiculoError(val message: String){
    class MatriculaNoValida(message: String): VehiculoError("La matricula '$message', no es válida.")
    class MatriculaYaExiste(message: String): VehiculoError("La matricula '$message', ya existe por lo que no se puede elegir.")
    class MarcaNoValida(message: String): VehiculoError("La marca '$message', no es válida.")
    class ModeloNoValido(message: String): VehiculoError("El modelo '$message', no es válido.")
    class KilometroNoValido(message: String): VehiculoError("Los kilometros '$message', no son válidos.")
    class FechaMatriculacionNoValida(message: LocalDate): VehiculoError("La fecha de matriculación '$message', no es válida.")
    class SameDateUpdate(message: Long): VehiculoError("No has cambiado ningún dato al editar el vehículo de id: $message")
}
