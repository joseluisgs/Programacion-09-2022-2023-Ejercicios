package es.sergiomisas.juegodelasbolas

import es.sergiomisas.juegodelasbolas.data.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage

class HelloApplication : Application() {


    override fun start(primaryStage: Stage) {
        RoutesManager.apply {
            app = this@HelloApplication
        }
        RoutesManager.initMainStage(primaryStage)
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
