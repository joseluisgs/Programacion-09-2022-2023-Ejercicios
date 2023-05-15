package com.example.ejerciciobolaschinasivanrc.error

sealed class Acierto(val message: String) {
    class JugadorGano(bolasOrd: Int, bolasJug: Int, bolas: Int) :
        Acierto("El ordenador eligio $bolasOrd bolas, el jugador eligio $bolasJug bolas,\nhabía $bolas bolas, por lo que el jugador ganó la ronda.")
}