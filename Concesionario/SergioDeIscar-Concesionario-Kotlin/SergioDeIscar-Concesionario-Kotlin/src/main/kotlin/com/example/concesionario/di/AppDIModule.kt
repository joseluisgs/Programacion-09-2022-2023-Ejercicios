package com.example.concesionario.di

import com.example.concesionario.repositories.vehicle.VehicleRepository
import com.example.concesionario.repositories.vehicle.VehicleRepoDataBase
import com.example.concesionario.services.storage.vehicle.VehicleFileJson
import com.example.concesionario.services.storage.vehicle.VehicleStorage
import com.example.concesionario.viewmodel.VehicleViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.example.concesionario.services.database.DataBaseManager
import com.example.concesionario.config.AppConfig
import com.example.concesionario.services.storage.vehicle.VehicleFileCsv
import com.example.concesionario.viewmodel.FormViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind

val AppDIModule = module {
    singleOf(::AppConfig)
    singleOf(::DataBaseManager)
    singleOf(::VehicleRepoDataBase){
        bind<VehicleRepository>()
    }
    single(named("JSON")) { VehicleFileJson() } bind VehicleStorage::class
    single(named("CSV")) { VehicleFileCsv() } bind VehicleStorage::class
    singleOf(::VehicleViewModel)
    singleOf(::FormViewModel)
}

