package dev.kevin.vehiculosfx.di

import dev.kevin.vehiculosfx.config.ConfigApp
import dev.kevin.vehiculosfx.repositories.VehiculoRepository
import dev.kevin.vehiculosfx.repositories.VehiculoRepositoryImpl
import dev.kevin.vehiculosfx.services.database.SqlDeLightClient
import dev.kevin.vehiculosfx.services.storage.StorageVehiculos
import dev.kevin.vehiculosfx.services.storage.StorageVehiculosImpl
import dev.kevin.vehiculosfx.viewmodels.VehiculoViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind


val AppDiModule = module {
    single{ ConfigApp() }

    single <VehiculoRepository> {VehiculoRepositoryImpl(get())}

    single {SqlDeLightClient(get())}

    single<StorageVehiculos> {StorageVehiculosImpl(get())}

    single {VehiculoViewModel(get(), get())}
}