package com.example.piedrapapeltijera.controllers

import com.example.piedrapapeltijera.PiedraPapelTijeraApplication
import com.example.piedrapapeltijera.routes.RoutesManager.reglasStage
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import kotlin.concurrent.thread

class PiedraPapelTijeraController {
    @FXML
    private lateinit var botonPiedra: Button

    @FXML
    private lateinit var botonPapel: Button

    @FXML
    private lateinit var botonTijera: Button

    @FXML
    private lateinit var botonLagarto: Button

    @FXML
    private lateinit var botonSpock: Button

    @FXML
    private lateinit var botonReglas: Button

    @FXML
    private lateinit var jugadaJugador: ImageView

    @FXML
    private lateinit var jugadaPc: ImageView

    @FXML
    private lateinit var tabla: GridPane

    @FXML
    private lateinit var textoFinal: TextField

    private val iconos: Array<Image> = arrayOf(
        Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/piedra.png")),
        Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/papel.png")),
        Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/tijera.png")),
        Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/lagarto.png")),
        Image(PiedraPapelTijeraApplication::class.java.getResourceAsStream("icons/spock.png"))
    )

    private val estilos: Map<String, String> = mapOf(
        "botonEnPiedra" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonEnPapel" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonEnTijera" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonEnLagarto" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonEnSpock" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraPiedra" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraPapel" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraTijera" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraLagarto" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraSpock" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonFueraReglas" to "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;",
        "botonEnReglas" to "-fx-background-color: #93D5C7; -fx-border-color: #93D5C7; -fx-background-radius: 7; -fx-border-radius: 7;"
    )

    @FXML
    private fun initialize() {
        iniciarBotonPiedra()
        iniciarBotonPapel()
        iniciarBotonTijera()
        iniciarBotonLagarto()
        iniciarBotonSpock()
        iniciarBotonReglas()
        tabla.style = "-fx-border-color: #93D5C7; -fx-border-radius: 10; -fx-border-width: 1.5;"
        textoFinal.style = "-fx-background-color: #E2FFF9; -fx-border-color: #93D5C7; -fx-border-radius: 10;"
    }

    private fun iniciarBotonReglas() {
        botonReglas.style = estilos["botonFueraReglas"]
    }

    private fun iniciarBotonPiedra() {
        val icono = ImageView(iconos[0])
        icono.fitWidth = 30.0
        icono.fitHeight = 30.0
        botonPiedra.graphic = icono
        botonPiedra.style = estilos["botonFueraPiedra"]
    }

    private fun iniciarBotonPapel() {
        val icono = ImageView(iconos[1])
        icono.fitWidth = 30.0
        icono.fitHeight = 30.0
        botonPapel.graphic = icono
        botonPapel.style = estilos["botonFueraPapel"]
    }

    private fun iniciarBotonTijera() {
        val icono = ImageView(iconos[2])
        icono.fitWidth = 30.0
        icono.fitHeight = 30.0
        botonTijera.graphic = icono
        botonTijera.style = estilos["botonFueraTijera"]
    }

    private fun iniciarBotonLagarto() {
        val icono = ImageView(iconos[3])
        icono.fitWidth = 30.0
        icono.fitHeight = 30.0
        botonLagarto.graphic = icono
        botonLagarto.style = estilos["botonFueraLagarto"]
    }

    private fun iniciarBotonSpock() {
        val icono = ImageView(iconos[4])
        icono.fitWidth = 30.0
        icono.fitHeight = 30.0
        botonSpock.graphic = icono
        botonSpock.style = estilos["botonFueraSpock"]
    }

    @FXML
    private fun onRatonEnPiedra() {
        botonPiedra.style = estilos["botonEnPiedra"]
    }

    @FXML
    private fun onRatonFueraPiedra() {
        botonPiedra.style = estilos["botonFueraPiedra"]
    }

    @FXML
    private fun onRatonEnPapel() {
        botonPapel.style = estilos["botonEnPapel"]
    }

    @FXML
    private fun onRatonFueraPapel() {
        botonPapel.style = estilos["botonFueraPapel"]
    }

    @FXML
    private fun onRatonEnTijera() {
        botonTijera.style = estilos["botonEnTijera"]
    }

    @FXML
    private fun onRatonFueraTijera() {
        botonTijera.style = estilos["botonFueraTijera"]
    }

    @FXML
    private fun onRatonEnLagarto() {
        botonLagarto.style = estilos["botonEnLagarto"]
    }

    @FXML
    private fun onRatonFueraLagarto() {
        botonLagarto.style = estilos["botonFueraLagarto"]
    }

    @FXML
    private fun onRatonEnSpock() {
        botonSpock.style = estilos["botonEnSpock"]
    }

    @FXML
    private fun onRatonFueraSpock() {
        botonSpock.style = estilos["botonFueraSpock"]
    }

    @FXML
    private fun onRatonEnReglas() {
        botonReglas.style = estilos["botonEnReglas"]
    }

    @FXML
    private fun onRatonFueraReglas() {
        botonReglas.style = estilos["botonFueraReglas"]
    }

    @FXML
    private fun onBotonReglasClick() {
        reglasStage()
    }

    @FXML
    private fun onBotonPiedraClick() {
        jugada(iconos[0])
    }

    @FXML
    private fun onBotonPapelClick() {
        jugada(iconos[1])
    }

    @FXML
    private fun onBotonTijeraClick() {
        jugada(iconos[2])
    }

    @FXML
    private fun onBotonLagartoClick() {
        jugada(iconos[3])
    }

    @FXML
    private fun onBotonSpockClick() {
        jugada(iconos[4])
    }

    private fun jugada(manoJugador: Image) {
        val manoPc = iconos.random()
        jugadaJugador.image = manoJugador
        jugadaPc.image = manoPc
        if (manoJugador == iconos[0] && manoPc == iconos[0]) {
            textoFinal.text = "EMPATE"
        }
        if (manoJugador == iconos[0] && manoPc == iconos[1]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[0] && manoPc == iconos[2]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[0] && manoPc == iconos[3]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[0] && manoPc == iconos[4]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[1] && manoPc == iconos[0]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[1] && manoPc == iconos[1]) {
            textoFinal.text = "EMPATE"
        }
        if (manoJugador == iconos[1] && manoPc == iconos[2]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[1] && manoPc == iconos[3]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[1] && manoPc == iconos[4]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[2] && manoPc == iconos[0]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[2] && manoPc == iconos[1]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[2] && manoPc == iconos[2]) {
            textoFinal.text = "EMPATE"
        }
        if (manoJugador == iconos[2] && manoPc == iconos[3]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[2] && manoPc == iconos[4]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[3] && manoPc == iconos[0]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[3] && manoPc == iconos[1]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[3] && manoPc == iconos[2]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[3] && manoPc == iconos[3]) {
            textoFinal.text = "EMPATE"
        }
        if (manoJugador == iconos[3] && manoPc == iconos[4]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[4] && manoPc == iconos[0]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[4] && manoPc == iconos[1]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[4] && manoPc == iconos[2]) {
            textoFinal.text = "HAS GANADO"
        }
        if (manoJugador == iconos[4] && manoPc == iconos[3]) {
            textoFinal.text = "HAS PERDIDO"
        }
        if (manoJugador == iconos[4] && manoPc == iconos[4]) {
            textoFinal.text = "EMPATE"
        }
        comprobarRespuesta()
    }

    private fun comprobarRespuesta() {
        if (textoFinal.text == "HAS GANADO") {
            textoFinal.style = "-fx-background-color: #8CF494; -fx-background-radius: 10"
        }
        if (textoFinal.text == "HAS PERDIDO") {
            textoFinal.style = "-fx-background-color: #FFB8B8; -fx-background-radius: 10"
        }
        if (textoFinal.text == "EMPATE") {
            textoFinal.style = "-fx-background-color: #9CBDB6; -fx-background-radius: 10"
        }
    }

}