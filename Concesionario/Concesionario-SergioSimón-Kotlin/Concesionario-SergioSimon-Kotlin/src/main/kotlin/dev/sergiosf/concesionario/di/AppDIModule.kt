package dev.sergiosf.concesionario.di

import dev.sergiosf.concesionario.config.AppConfig
import dev.sergiosf.concesionario.repositories.VehiculoRepository
import dev.sergiosf.concesionario.repositories.VehiculoRepositoryImpl
import dev.sergiosf.concesionario.service.storage.StorageVehiculos
import dev.sergiosf.concesionario.service.storage.StorageVehiculosImp
import dev.sergiosf.concesionario.service.database.DataBaseService
import dev.sergiosf.concesionario.viewmodels.VehiculosViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AppDIModule = module {
    singleOf(::AppConfig)

    singleOf(::DataBaseService)

    singleOf(::VehiculoRepositoryImpl) {
        bind<VehiculoRepository>()
    }

    singleOf(::StorageVehiculosImp) {
        bind<StorageVehiculos>()
    }

    singleOf(::VehiculosViewModel)
}