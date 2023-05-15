package oscargrc.chinos

import dev.joseluisgs.moscafx.di.AppDIModule
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger
import oscargrc.chinos.routes.RoutesManager

private val logger = KotlinLogging.logger {  }
class ChinosApplication : Application() {
    override fun start(stage: Stage) {
        logger.info { "Iniciando  App" }

        // Cargo el contexto de Koin de DI
        startKoin {
            slf4jLogger() // Logger de Koin para ver lo que hace opcional
            modules(AppDIModule)
        }


        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@ChinosApplication
        }
        // Inicializamos la escena principal
        RoutesManager.initMainStage(stage)
    }
}

fun main() {
    Application.launch(ChinosApplication::class.java)
}