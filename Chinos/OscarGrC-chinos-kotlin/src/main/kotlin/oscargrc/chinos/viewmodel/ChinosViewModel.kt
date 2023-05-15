package dev.joseluisgs.moscafx.viewmodel

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging
import oscargrc.chinos.models.ChinosState


private val logger = KotlinLogging.logger {}

// No me apetece crearme modelos y errores
// Tú no lo hagas así en la misma clase ;)

sealed class ChinosError {
    object NoAcertado : ChinosError()
    class Casi(val fila: Int, val columna: Int, val intentos: Int) : ChinosError()
}


class ChinosViewModel {
    // Creo el estado con la imagen inicial
    val state = ChinosState()
    // tenemos que crear una matriz para controlar las imagenes mostrada
    var matrix = Array(3) { IntArray(3) }

    init {
        logger.info { "ChinosViewModel cargando datos" }
    }

    fun iniciarJuego(chinosMax: Int, rondas: Int) {
        logger.info { "Iniciando juego "}
        state.numeroChinosMax.set(chinosMax)
        state.numeroRondas.set(rondas)


    }



    // Clase que representa el estado de la vista

}
