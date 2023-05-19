package dev.sbytmacke.matrix.models

import dev.sbytmacke.matrix.models.base.IInfectCharacter
import java.time.LocalDate
import java.util.*

class InfectCharacter(
    id: UUID, name: String, location: String, latitude: Int, longitude: Int, cityNac: String, age: Int,
    dateCreate: LocalDate, override val capacityInfect: Int
) : Character(
    id, name, location, latitude,
    longitude,
    cityNac,
    age, dateCreate
), IInfectCharacter {

    fun infect(genericChar: GenericCharacter): InfectCharacter {
        return InfectCharacter(
            genericChar.id,
            genericChar.name,
            genericChar.location,
            genericChar.latitude,
            genericChar.longitude,
            genericChar.cityNac,
            genericChar.age,
            LocalDate.now(),
            (1..8).random()
        )
    }

    override fun get(): Character {
        return this
    }

    override fun show(): String {
        return "InfectCharacter(id=$id, name='$name', location='$location', latitude=$latitude, " +
                "longitude=$longitude, cityNac='$cityNac', age=$age, dateCreate=$dateCreate, probabilityInfect=$capacityInfect)"
    }
}