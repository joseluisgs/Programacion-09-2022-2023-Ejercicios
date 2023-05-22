package dev.kevin.vehiculosfx.errors


sealed class VehiculoError(val message: String) {
    class SaveImage(message: String): VehiculoError(message)
    class LoadImage(message: String): VehiculoError(message)
    class DeleteImage(message: String): VehiculoError(message)
    class DeleteAllImages(message: String): VehiculoError(message)
    class UpdateImage(message: String): VehiculoError(message)
    class NotFoundImage(message: String):VehiculoError(message)
    class ValidationError(message: String):VehiculoError(message)
}