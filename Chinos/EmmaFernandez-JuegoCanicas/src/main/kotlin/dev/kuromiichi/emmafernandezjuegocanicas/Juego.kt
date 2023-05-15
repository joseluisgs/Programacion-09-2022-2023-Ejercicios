package dev.kuromiichi.emmafernandezjuegocanicas

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import kotlin.system.exitProcess

object Juego {
    var rondasGanadas = SimpleStringProperty("0")
    var rondasPerdidas = SimpleStringProperty("0")
    var puntosJugador = SimpleStringProperty("0")
    var puntosCpu = SimpleStringProperty("0")
    private var numCanicas = 0

    fun iniciarRonda() {
        puntosJugador.value = "0"
        puntosCpu.value = "0"
    }

    fun calcularResultado(numero: Int) {
        numCanicas = listOf(3, 5, 7).random()
        when (numero == numCanicas) {
            true -> sumarPuntoJugador()
            false -> sumarPuntoCpu()
        }
        when {
            puntosJugador.value == "3" -> {
                rondasGanadas.value = (rondasGanadas.value.toInt() + 1).toString()
                resumenRonda()
                iniciarRonda()
            }

            puntosCpu.value == "3" -> {
                rondasPerdidas.value = (rondasPerdidas.value.toInt() + 1).toString()
                resumenRonda()
                iniciarRonda()
            }
        }
    }

    private fun sumarPuntoJugador() {
        puntosJugador.value = (puntosJugador.value.toInt() + 1).toString()
        val alert = Alert(AlertType.INFORMATION)
        alert.title = "Acierto"
        alert.headerText = "Acertaste el número de canicas"
        alert.contentText = "El rival tenía $numCanicas canicas"
        alert.showAndWait()
    }

    private fun sumarPuntoCpu() {
        puntosCpu.value = (puntosCpu.value.toInt() + 1).toString()
        val alert = Alert(AlertType.INFORMATION)
        alert.title = "Fallo"
        alert.headerText = "Fallaste el número de canicas"
        alert.contentText = "El rival tenía $numCanicas canicas"
        alert.showAndWait()
    }

    private fun resumenRonda() {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Resumen de la ronda"
        alert.headerText = if (puntosJugador.value == "3") "Has ganado la ronda" else "Has perdido la ronda"
        alert.contentText = "Puntos Jugador: ${puntosJugador.value}\nPuntos Rival: ${puntosCpu.value}\n\n" +
                "¿Jugar otra ronda?"
        val res = alert.showAndWait()
        when (res.get()) {
            ButtonType.CANCEL -> exitProcess(0)
        }
    }
}
