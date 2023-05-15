package dev.kuromiichi.emmafernandezjuegocanicas.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label

class InstruccionesController {
    @FXML
    private lateinit var labelInstrucciones: Label

    @FXML
    private fun initialize() {
        labelInstrucciones.text = """
            El juego consiste en adivinar el número de canicas que posee el rival.
            
            El rival tendrá un número aleatorio de canicas entre 3, 5 y 7. Usando el
            selector, el jugador puede elegir una cantidad de canicas. Al pulsar el
            botón "Confirmar", se comprobará si la cantidad elegida se corresponde
            con la cantidad de canicas del rival. En caso de acertar, el jugador
            gana un punto. En caso contrario, el rival gana un punto.
            
            Las rondas se juegan al mejor de cinco, es decir, el jugador que llegue 
            primero a tres puntos gana. Se jugarán tantas rondas como el jugador desee.
        """.trimIndent()
    }
}
