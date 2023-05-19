package com.example.vehiculoscrudinterface.services.storage.vehiculo

import com.example.vehiculoscrudinterface.mappers.toVehiculoListDto
import com.example.vehiculoscrudinterface.models.Vehiculo
import mu.KotlinLogging
import java.io.File

//private val logger = KotlinLogging.logger{}
//
//object XmlService: VehiculoService {
//
//    private val dataPath = File("${System.getProperty("user.dir")}${File.separator}data")
//    private val xmlFile = File(dataPath, "vehiculos.xml")
//
//    private val serializer = Persister()
//
//    init {
//        if (!dataPath.exists()) {
//            logger.debug { "VehiculoXmlService -> Creando directorio DATA" }
//            dataPath.mkdir()
//        }
//        if (!xmlFile.exists()) {
//            logger.debug { "VehiculoXmlService -> Creando fichero XML" }
//            xmlFile.createNewFile()
//        }
//    }
//
//    override fun exportar(items: List<Vehiculo>) {
//        serializer.write(items.toVehiculoListDto(), xmlFile)
//    }
//
//    override fun importar(): List<Vehiculo> {
//        TODO("Not yet implemented")
//    }
//}