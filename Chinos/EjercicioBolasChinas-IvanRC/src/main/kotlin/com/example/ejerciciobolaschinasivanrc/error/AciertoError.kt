package com.example.ejerciciobolaschinasivanrc.error

sealed class AciertoError(val message: String) {
    class HuboEmpate(bolasOrd: Int, bolasJug: Int, bolas: Int) :
        AciertoError("El ordenador eligio $bolasOrd bolas, el jugador eligio $bolasJug bolas,\npero en realidad había $bolas bolas.")
    class OrdenadorGano(bolasOrd: Int, bolasJug: Int, bolas: Int) :
        AciertoError("El ordenador eligio $bolasOrd bolas, el jugador eligio $bolasJug bolas,\nhabía $bolas bolas, por lo que el ordenador ganó la ronda.")
    class MismaOpcion(bolasOrd: Int, bolasJug: Int) :
        AciertoError("El ordenador eligio $bolasOrd bolas y por ende el jugador no puede\neligir $bolasJug bolas, la ronda quedo invalidada, por lo que se repite.")
}
