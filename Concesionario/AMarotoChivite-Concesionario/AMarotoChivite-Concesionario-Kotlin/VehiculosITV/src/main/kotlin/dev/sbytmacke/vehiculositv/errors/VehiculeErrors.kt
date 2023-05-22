package dev.sbytmacke.vehiculositv.errors

sealed class VehiculeError(val message: String) {
    /*    class LoadJson(message: String) : AlumnoError(message)
        class SaveJson(message: String) : AlumnoError(message)*/

    class IdInvalid(message: String) : VehiculeError(message)
    class MatriculaInvalid(message: String) : VehiculeError(message)
    class MarcaInvalid(message: String) : VehiculeError(message)
    class ModeloInvalid(message: String) : VehiculeError(message)
    class TypeMotorInvalid(message: String) : VehiculeError(message)
    class KmInvalid(message: String) : VehiculeError(message)
    class FechaMantenimientoInvalid(message: String) : VehiculeError(message)
    class NotFound(message: String) : VehiculeError(message)
    class EmptyField : VehiculeError("Campos vacíos")
    class ExportInvalid : VehiculeError("Exportación fallida")
}
