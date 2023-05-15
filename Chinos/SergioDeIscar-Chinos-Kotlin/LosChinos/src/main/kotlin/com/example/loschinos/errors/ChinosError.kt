package com.example.loschinos.errors

sealed class ChinosError(val message: String) {
    class ApuestaPerdida(apuesta: Int, valor: Int, seleccionIa: Int) : ChinosError("Has apostado $apuesta y el total de bolas era $valor, la IA selecciono $seleccionIa bolas.")
    class ApuestaNadieGana(apuesta: Int, iaApuesta: Int) : ChinosError("Has apostado $apuesta y la IA ha apostado $iaApuesta, nadie gana.")
    class ApuestaEmpate(apuesta: Int, iaApuesta: Int, valor: Int, seleccionIa: Int) : ChinosError("Has apostado $apuesta y la IA ha apostado $iaApuesta, el total de bolas $valor empate (la IA selecciono $seleccionIa bolas).")
}