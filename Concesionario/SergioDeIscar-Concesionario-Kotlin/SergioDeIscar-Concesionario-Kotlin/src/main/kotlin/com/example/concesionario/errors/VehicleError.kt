package com.example.concesionario.errors

sealed class VehicleError(val message: String) {
    class IdNoValido(id: Long) : VehicleError("El id $id no es valido.")
    class MarcaNoValida(marca: String) : VehicleError("La marca \"$marca\" no es valida.")
    class ModeloNoValido(modelo: String) : VehicleError("El modelo \"$modelo\" no es valido.")
    class MotorNoValido(motor: String) : VehicleError("El motor \"$motor\" no es valido.")
    class FechaNoValida(fecha: String) : VehicleError("La fecha \"$fecha\" no es valida.")
    class ImagenURLNoValida(imagenURL: String) : VehicleError("La imagenURL \"$imagenURL\" no es valida.")

    class VehiculoNoEncontrado(id: Long) : VehicleError("El vehiculo con id $id no se ha encontrado.")
    class VehiculoNoCreado(id: Long) : VehicleError("El vehiculo con id $id no se ha podido crear.")
    class VehiculoNoActualizado(id: Long) : VehicleError("El vehiculo con id $id no se ha podido actualizar.")
    class VehiculoNoBorrado(id: Long) : VehicleError("El vehiculo con id $id no se ha podido borrar.")

    class ImportError(type: String) : VehicleError("Error al importar el $type.")
    class ExportError(type: String) : VehicleError("Error al exportar el $type.")
}