package dev.sbytmacke.thechinos.states

import javafx.beans.property.SimpleIntegerProperty

object AppState {
    var rounds: Int = 0
    var countIAWins = SimpleIntegerProperty()
    var countPlayerWins = SimpleIntegerProperty()
}