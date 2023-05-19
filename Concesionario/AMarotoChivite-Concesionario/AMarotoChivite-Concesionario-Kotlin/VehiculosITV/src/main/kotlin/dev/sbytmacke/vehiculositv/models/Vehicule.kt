package dev.sbytmacke.vehiculositv.models

data class Vehicule(
    val id: Long = NEW_VEHICULE,
    val matricule: String,
    val marca: String,
    val modelo: String,
    val typeMotor: TypeMotor,
    val km: Double,
    val fechaMantenimiento: String,
) {
    companion object {
        const val NEW_VEHICULE = -1L
    }

    fun isNewVehicule(): Boolean {
        return id == NEW_VEHICULE
    }

    enum class TypeMotor(val value: String) {
        ELECTRICO("Eléctrico"),
        HIBRIDO("Híbrido"),
        DIESEL("Diésel"),
        GASOLINA("Gaosolina"),
        NONE("None"),
        ALL("Todos")
    }
}
