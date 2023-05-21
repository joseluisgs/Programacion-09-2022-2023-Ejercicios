package dev.sergiosf.concesionario

import dev.sergiosf.concesionario.di.AppDIModule
import dev.sergiosf.concesionario.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class ConcesionarioApplication : Application() {

//    init {
//        // creamos Koin
//        startKoin {
//            printLogger() // Logger de Koin
//            modules(AppDIModule) // Módulos de Koin
//        }
//    }

    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(AppDIModule)

        }
        RoutesManager.apply {
            app = this@ConcesionarioApplication
        }
        RoutesManager.initMainStage(stage)

    }
}

fun main() {
    Application.launch(ConcesionarioApplication::class.java)
}