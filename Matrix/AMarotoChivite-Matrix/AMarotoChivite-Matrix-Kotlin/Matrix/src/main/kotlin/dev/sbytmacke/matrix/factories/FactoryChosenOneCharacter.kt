package dev.sbytmacke.matrix.factories

import dev.sbytmacke.matrix.models.ChosenOneCharacter
import java.time.LocalDate
import java.util.*

class FactoryChosenOneCharacter {
    companion object {
        // Posteriormente deberemos actualizar la location
        fun generate(): ChosenOneCharacter {
            val randomUUID: UUID = UUID.randomUUID()
            val name: String = "Neo"
            val location: String = "x"
            val cityName: String = randomCityName()
            val randomAge: Int = (15..99).random()
            val probIsChosenOne: Int = 50
            return ChosenOneCharacter(
                randomUUID,
                name,
                location,
                0,
                0,
                cityName,
                randomAge,
                LocalDate.now(),
                probIsChosenOne
            )
        }

        /**
         * Genera de manera aleatoria un nombre de ciudad dentro de nuestro catálogo
         *
         * @return storageCities[indexRandom], es el nombre aleatorio
         */
        private fun randomCityName(): String {
            val indexRandom = (0..5).random()
            val storageCities = arrayOf("Nueva York", "Pekin", "Roma", "Paris", "Londres", "Caracuel")
            return storageCities[indexRandom]
        }
    }
}