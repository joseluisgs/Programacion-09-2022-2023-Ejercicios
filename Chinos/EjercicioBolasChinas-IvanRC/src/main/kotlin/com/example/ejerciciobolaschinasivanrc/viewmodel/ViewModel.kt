package com.example.ejerciciobolaschinasivanrc.viewmodel

import com.example.ejerciciobolaschinasivanrc.error.Acierto
import com.example.ejerciciobolaschinasivanrc.error.AciertoError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }

class ViewModel {

    val state = SharedState()

    init {
        logger.debug { "Se inicializa el ViewModel" }
    }

    fun intentarAdivinar(): Result<Acierto, AciertoError> {
        logger.debug { "Se juega al juego de las adivinanzas" }

        val jugador = state.opcionJugador
        val ordenador = state.opcionOrdenador
        val bolas = state.bolasJugador + state.bolasOrdenador

        if(jugador == ordenador){
            return Err(AciertoError.MismaOpcion(ordenador, jugador))
        }

        if(ordenador == bolas){
            return Err(AciertoError.OrdenadorGano(ordenador, jugador, bolas))
        }

        if(ordenador != bolas && jugador != bolas){
            return Err(AciertoError.HuboEmpate(ordenador, jugador, bolas))
        }

        return Ok(Acierto.JugadorGano(ordenador, jugador, bolas))
    }
}
