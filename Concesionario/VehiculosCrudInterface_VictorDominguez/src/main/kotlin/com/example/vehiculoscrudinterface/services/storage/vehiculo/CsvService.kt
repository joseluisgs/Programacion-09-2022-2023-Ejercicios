package com.example.vehiculoscrudinterface.services.storage.vehiculo

import com.example.vehiculoscrudinterface.ConcesionarioApplication
import com.example.vehiculoscrudinterface.enums.TipoMotor
import com.example.vehiculoscrudinterface.models.Vehiculo
import javafx.scene.image.Image
import mu.KLogger
import mu.KotlinLogging
import java.io.File
import java.time.LocalDate
import java.util.*

private val logger = KotlinLogging.logger{}

object CsvService: VehiculoService {

    private val path = "${System.getProperty("user.dir")}${File.separator}src${File.separator}main${File.separator}resources${File.separator}vehiculos.csv"
    private val fichero = File(path)
    private val iconos = listOf<String>(
        "icons/cocheAmarillo.png",
        "icons/cocheAzul.png",
        "icons/cocheAzulOscuro.png",
        "icons/cocheBlanco.png",
        "icons/cocheMorado.png",
        "icons/cocheNegro.png",
        "icons/cocheRojo.png",
        "icons/cocheRosa.png",
        "icons/cocheVerde.png"
    )

    private val dataPath = File("${System.getProperty("user.dir")}${File.separator}data")
    private val csvFile = File(dataPath, "vehiculos.csv")

    init {
        if (!dataPath.exists()) {
            logger.debug { "VehiculoCsvService -> Creando directorio DATA" }
            dataPath.mkdir()
        }
        if (!csvFile.exists()) {
            logger.debug { "VehiculoCsvService -> Creando fichero CSV" }
            csvFile.createNewFile()
        }
    }

    override fun exportar(items: List<Vehiculo>) {
        logger.debug { "VehiculoCsvService -> Exportando datos a CSV" }
        csvFile.writeText("ID;Marca;Modelo;Tipo de motor;Kilometraje;Fecha de matriculación;Imagen\n")
        items.forEach {
            csvFile.appendText("${it.id};${it.marca};${it.modelo};${it.tipoMotor};${it.km};${it.fechaMatriculacion};${it.imagen}\n")
        }
    }

    override fun importar(): List<Vehiculo> {
        logger.debug { "VehiculoCsvService -> Importando datos de CSV" }
        return fichero.readLines()
            .drop(1)
            .map { linea -> linea.split(";") }
            .map { columnas ->
                Vehiculo(
                    columnas[0],
                    columnas[1],
                    columnas[2],
                    TipoMotor.valueOf(columnas[3]),
                    columnas[4].toInt(),
                    LocalDate.parse(columnas[5]),
                    iconos.random()
                )
            }
    }

}