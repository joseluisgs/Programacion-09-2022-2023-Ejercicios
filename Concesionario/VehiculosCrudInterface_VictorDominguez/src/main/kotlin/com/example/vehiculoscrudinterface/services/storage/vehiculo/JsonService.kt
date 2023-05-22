package com.example.vehiculoscrudinterface.services.storage.vehiculo
//
//import com.example.vehiculoscrudinterface.dto.VehiculoDto
//import com.example.vehiculoscrudinterface.mappers.toVehiculoListDto
//import com.example.vehiculoscrudinterface.models.Vehiculo
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.adapter
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import mu.KotlinLogging
//import java.io.File
//
//private val logger = KotlinLogging.logger{}
//
//@OptIn(ExperimentalStdlibApi::class)
//object JsonService: VehiculoService {
//
//    private val dataPath = File("${System.getProperty("user.dir")}${File.separator}data")
//    private val jsonFile = File(dataPath, "vehiculos.json")
//    private val adapter = Moshi.Builder()
//        .addLast(KotlinJsonAdapterFactory())
//        .build().adapter<List<VehiculoDto>>()
//
//    init {
//        if (!dataPath.exists()) {
//            logger.debug { "VehiculoJsonService -> Creando directorio DATA" }
//            dataPath.mkdir()
//        }
//        if (!jsonFile.exists()) {
//            logger.debug { "VehiculoJsonService -> Creando fichero JSON" }
//            jsonFile.createNewFile()
//        }
//    }
//
//    override fun exportar(items: List<Vehiculo>) {
//        jsonFile.writeText(adapter.indent("  ").toJson(items.toVehiculoListDto()))
//    }
//
//    override fun importar(): List<Vehiculo> {
//        TODO("Not yet implemented")
//    }
//}