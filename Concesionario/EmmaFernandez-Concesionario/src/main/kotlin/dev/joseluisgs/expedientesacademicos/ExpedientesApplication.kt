package dev.joseluisgs.expedientesacademicos

import dev.joseluisgs.expedientesacademicos.di.AppDIModule
import dev.joseluisgs.expedientesacademicos.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ExpedientesApplication : Application(), KoinComponent {

    init {
        startKoin {
            printLogger()
            modules(AppDIModule)
        }
    }

    override fun start(stage: Stage) {
        RoutesManager.apply {
            app = this@ExpedientesApplication
        }.run {
            initMainStage(stage)
        }

    }
}

fun main() {
    Application.launch(ExpedientesApplication::class.java)
}
