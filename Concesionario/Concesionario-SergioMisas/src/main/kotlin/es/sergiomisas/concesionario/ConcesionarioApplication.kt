package es.sergiomisas.concesionario

import es.sergiomisas.concesionario.di.AppDIModule
import es.sergiomisas.concesionario.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ConcesionarioApplication : Application(), KoinComponent {

    init {
        // creamos Koin
        startKoin {
            printLogger() // Logger de Koin
            modules(AppDIModule) // Módulos de Koin
        }
    }

    // Cuando se inicia la aplicación
    override fun start(stage: Stage) {


        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@ConcesionarioApplication
        }.run {
            // Iniciamos la aplicación, podiamos hacerlo con also!!
            initMainStage(stage)
        }

    }

    // Cuando se para la aplicación
    /*
    override fun stop() {
        // No hacemos nada
    }
    */
}

fun main() {
    // No hagas nada a aquí porque abre un hilo de ejecución
    Application.launch(ConcesionarioApplication::class.java)

}
