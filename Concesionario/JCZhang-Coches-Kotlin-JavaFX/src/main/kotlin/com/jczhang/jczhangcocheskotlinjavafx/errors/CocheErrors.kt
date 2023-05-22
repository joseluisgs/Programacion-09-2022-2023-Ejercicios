package com.jczhang.jczhangcocheskotlinjavafx.errors

sealed class CocheErrors(val message: String) {
    class MarcaNoValida(message: String) : CocheErrors(message)
    class ModeloNoValido(message: String) : CocheErrors(message)
    class KmNoValido(message: String) : CocheErrors(message)
    class FechaInvalida(message: String) : CocheErrors(message)
    class TipoNoValido(message: String): CocheErrors(message)

}