package com.example.vehiculoscrudinterface.errors

import java.lang.Error
import java.util.StringJoiner

sealed class VehiculoError(message: String): Error(message) {
    class VehiculoNoValidoError(message: String): VehiculoError("Vehículo no válido: $message")
}