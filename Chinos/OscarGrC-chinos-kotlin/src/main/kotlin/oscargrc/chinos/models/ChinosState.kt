package oscargrc.chinos.models

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty

data class ChinosState(
    val numeroChinosMax: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val numeroRondas: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val chinosJugador: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val chinosIA: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val prediccionIA: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val prediccionJugador: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val victoria: SimpleBooleanProperty = SimpleBooleanProperty(false),
    val victoriasJugador: SimpleIntegerProperty = SimpleIntegerProperty(0),
    val victoriasIA: SimpleIntegerProperty = SimpleIntegerProperty(0),
)