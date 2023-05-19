package com.example.loschinos.viewmodels

import com.example.loschinos.errors.ChinosError
import com.example.loschinos.states.ChinosState
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class ChinosViewModel {
    val state = ChinosState()

    fun iaApuesta(){
        state.bolasIaSelect = (0..state.bolasMax.value).random()
        println("IA selecciona ${state.bolasIaSelect}/${state.bolasMax.value} bolas.")
    }

    fun apostar(): Result<String, ChinosError> {
        state.bolasIaApuesta = (state.bolasIaSelect..(state.bolasIaSelect + state.bolasMax.value)).random() // Apuesta de la IA, tiene en cuenta su selección

        val bolasTotal = state.bolasUserSelect.value + state.bolasIaSelect

        // Empate
        if (state.bolasUserApuesta.value == state.bolasIaApuesta && bolasTotal == state.bolasUserApuesta.value){
            state.rondas.value -= 1
            val iaSelectTemp = state.bolasIaSelect // Guardamos la selección de la IA para mostrarla en el mensaje
            iaApuesta()
            return Err(ChinosError.ApuestaEmpate(state.bolasUserApuesta.value, state.bolasIaApuesta, bolasTotal, iaSelectTemp))
        }

        // Gana Ia
        if (state.bolasIaApuesta == bolasTotal){
            state.victoriasIA.value += 1
            state.rondas.value -= 1
            val iaSelectTemp = state.bolasIaSelect // Guardamos la selección de la IA para mostrarla en el mensaje
            iaApuesta()
            return Err(ChinosError.ApuestaPerdida(state.bolasUserApuesta.value, bolasTotal, iaSelectTemp))
        }

        // Gana player
        if (state.bolasUserApuesta.value == bolasTotal){
            state.victoriasUser.value += 1
            state.rondas.value -= 1
            val iaSelectTemp = state.bolasIaSelect // Guardamos la selección de la IA para mostrarla en el mensaje
            iaApuesta()
            return Ok("Has apostado ${state.bolasUserApuesta.value} y el total de bolas era $bolasTotal, ganaste (la ia selecciono ${iaSelectTemp}).")
        }

        return Err(ChinosError.ApuestaNadieGana(state.bolasUserApuesta.value, state.bolasIaApuesta))
    }
}