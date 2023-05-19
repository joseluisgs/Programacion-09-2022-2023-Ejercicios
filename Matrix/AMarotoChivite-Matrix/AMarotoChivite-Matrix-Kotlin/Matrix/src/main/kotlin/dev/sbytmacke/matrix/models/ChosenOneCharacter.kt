package dev.sbytmacke.matrix.models

import dev.sbytmacke.matrix.models.base.IChosenOneCharacter
import java.time.LocalDate
import java.util.*

class ChosenOneCharacter(
    id: UUID, name: String, location: String, latitude: Int, longitude: Int, cityNac: String, age: Int,
    dateCreate: LocalDate, override val probabilityIsTheChosenOne: Int
) : Character(
    id, name, location, latitude,
    longitude,
    cityNac,
    age, dateCreate
), IChosenOneCharacter {

    fun isChosenOne(): Boolean {
        val random = (1..100).random()
        return random >= probabilityIsTheChosenOne
    }

    override fun get(): Character {
        return this
    }

    override fun show(): String {
        return "ChosenOneCharacter(id=$id, name='$name', location='$location', latitude=$latitude, " +
                "longitude=$longitude, cityNac='$cityNac', age=$age, dateCreate=$dateCreate, probabilityIsTheChosenOne=$probabilityIsTheChosenOne)"
    }
}