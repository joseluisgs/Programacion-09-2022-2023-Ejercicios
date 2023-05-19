package dev.sbytmacke.matrix.models

import java.time.LocalDate
import java.util.*

abstract class Character(
    val id: UUID,
    val name: String,
    var location: String,
    var latitude: Int,
    var longitude: Int,
    val cityNac: String,
    val age: Int,
    val dateCreate: LocalDate
) {
    fun updateLocation(row: Int, column: Int) {
        latitude = row
        longitude = column
        location = "$latitude$longitude"
    }
}