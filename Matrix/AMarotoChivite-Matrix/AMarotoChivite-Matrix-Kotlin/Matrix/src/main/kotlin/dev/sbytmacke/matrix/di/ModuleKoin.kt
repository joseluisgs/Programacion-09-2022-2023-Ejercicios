package dev.sbytmacke.matrix.di

import dev.sbytmacke.matrix.viewmodels.SimulationViewModel
import org.koin.dsl.module

val ModuleKoin = module {
    single { SimulationViewModel() }
}