package dev.sbytmacke.matrix.models

import dev.sbytmacke.matrix.models.base.IGenericCharacter
import java.time.LocalDate
import java.util.*

class GenericCharacter(
    id: UUID, name: String, location: String, latitude: Int, longitude: Int, cityNac: String, age: Int,
    dateCreate: LocalDate, override var probabilityAutoDead: Int
) : Character(
    id, name, location, latitude,
    longitude,
    cityNac,
    age, dateCreate
), IGenericCharacter {

    override fun get(): Character {
        return this
    }

    override fun show(): String {
        return "GenericCharacter(id=$id, name='$name', location='$location', latitude=$latitude, " +
                "longitude=$longitude, cityNac='$cityNac', age=$age, dateCreate=$dateCreate, probabilityAutoDead=$probabilityAutoDead)"
    }
}