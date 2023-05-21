package dev.sergiosf.concesionario.errors

sealed class VehiculoError(val message: String) {
    class ValidationProblem(message: String) : VehiculoError(message)
    class SaveImage(message: String) : VehiculoError(message)
    class LoadImage(message: String) : VehiculoError(message)
    class DeleteImage(message: String) : VehiculoError(message)
    class DeleteById(message: String) : VehiculoError(message)
    class NotFound(message: String) : VehiculoError(message)
}