package dev.sergiosf.chinos.state

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import java.util.Objects

data class ChinosState(
    var playerBolas: ObjectProperty<Int> = SimpleObjectProperty(0),
    var playerPredicion: ObjectProperty<Int> = SimpleObjectProperty(0),


    var intentoIA: ObjectProperty<Int> = SimpleObjectProperty(0),
    var bolasIA: ObjectProperty<Int> = SimpleObjectProperty(0),

    var rondas: ObjectProperty<Int> = SimpleObjectProperty(1),

    var empate: ObjectProperty<Int> = SimpleObjectProperty(0),
    var iaPutos: ObjectProperty<Int> = SimpleObjectProperty(0),
    var playerPuntos: ObjectProperty<Int> = SimpleObjectProperty(0),
)