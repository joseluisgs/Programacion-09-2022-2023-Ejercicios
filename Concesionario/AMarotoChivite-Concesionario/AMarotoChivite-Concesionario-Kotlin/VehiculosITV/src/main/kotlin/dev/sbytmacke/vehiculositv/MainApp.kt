package dev.sbytmacke.vehiculositv

import dev.sbytmacke.vehiculositv.controllers.MainViewController
import dev.sbytmacke.vehiculositv.di.ModuleKoin
import dev.sbytmacke.vehiculositv.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class MainApp : Application(), KoinComponent {

    override fun start(stage: Stage) {

        // Agregamos propiedad para almacenar la instancia del controlador
        //val mainVieController by inject<MainViewController>()

        startKoin {
            printLogger()
            modules(ModuleKoin)
        }

        // Inicializamos el manager de navegación (Routes) para encapsular los STAGES y SCENES
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}