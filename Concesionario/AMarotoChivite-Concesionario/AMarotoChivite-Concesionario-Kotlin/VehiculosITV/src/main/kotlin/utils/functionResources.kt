package utils

import dev.sbytmacke.vehiculositv.MainApp
import java.io.InputStream
import java.net.URL

// Util en el caso para leer del resources para ahorrarnos código

fun getResource(resource: String): URL {
    return MainApp::class.java.getResource(resource)
        ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
}

fun getResourceAsStream(resource: String): InputStream {
    return MainApp::class.java.getResourceAsStream(resource)
        ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
}