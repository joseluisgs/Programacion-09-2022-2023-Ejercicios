package dev.sbytmacke.matrix.factories

import dev.sbytmacke.matrix.models.GenericCharacter
import java.time.LocalDate
import java.util.*

class FactoryGenericCharacter {
    // Posteriormente deberemos actualizar la location
    companion object {
        fun generate(): GenericCharacter {
            val randomUUID: UUID = UUID.randomUUID()
            val name: String = randomName()
            val location: String = "x"
            val cityName: String = randomCityName()
            val randomAge: Int = (15..99).random()
            val probAutoDead: Int = (1..100).random()
            return GenericCharacter(
                randomUUID,
                name,
                location,
                0,
                0,
                cityName,
                randomAge,
                LocalDate.now(),
                probAutoDead
            )
        }

        /**
         * Genera de manera aleatoria un nombre dentro de nuestro catálogo
         *
         * @return storageNames[indexRandom], es el nombre aleatorio
         */
        private fun randomName(): String {
            val indexRandom = (0..6).random()
            val storageNames = arrayOf("Pepe", "Juan", "Ana", "Sonia", "Pedro", "Chiquito", "Elena")
            return storageNames[indexRandom]
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
