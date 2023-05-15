package com.example.ejerciciobolaschinasivanrc.viewmodel

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

data class SharedState(
    var bolasOrdenador: Int = 0,
    var bolasJugador: Int = 0,
    var opcionOrdenador: Int = 0,
    var opcionJugador: Int = 0,
    var rondasOrdenador: Int = 0,
    var rondasJugador: Int = 0,
    var rondas: Int = 0,
    var maxRondas: Int = 0,
    var maxBolas: Int = 0,
    val victOrdenador: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val victJugador: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val textView: SimpleStringProperty = SimpleStringProperty(""),
    var listaAcciones: MutableList<String> = mutableListOf<String>()
){
    fun addAccion(accion: String) {
        val max = 10
        if(listaAcciones.size < max){
            listaAcciones.add(accion)
        }else{
            listaAcciones.removeAt(0)
            listaAcciones.add(accion)
        }
        textView.value = listaAcciones.joinToString(separator = "\n")
    }
    // Otra versión que no va eliminando texto a la que añadas
    // fun addAccion(accion: String) {
    // listaAcciones.add(accion)
    // textView.value = listaAcciones.joinToString(separator = "\n")
    // }
}