package com.example.loschinos.states

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

data class ChinosState(
    // Bolas seleccionadas por el usuario
    var bolasUserSelect: ObjectProperty<Int> = SimpleObjectProperty(0),
    // Bolas apostadas por el usuario
    var bolasUserApuesta: ObjectProperty<Int> = SimpleObjectProperty(0),

    // Bolas seleccionadas por la IA
    var bolasIaSelect: Int = 0,
    // Bolas apostadas por la IA
    var bolasIaApuesta: Int = 0,

    // Número de rondas a jugar
    var rondas: ObjectProperty<Int> = SimpleObjectProperty(1),
    // Bolas máximas por cabeza por ronda
    var bolasMax: ObjectProperty<Int> = SimpleObjectProperty(5),

    // Victorias del usuario
    var victoriasUser: ObjectProperty<Int> = SimpleObjectProperty(0),
    // Victorias de la IA
    var victoriasIA: ObjectProperty<Int> = SimpleObjectProperty(0),
)
