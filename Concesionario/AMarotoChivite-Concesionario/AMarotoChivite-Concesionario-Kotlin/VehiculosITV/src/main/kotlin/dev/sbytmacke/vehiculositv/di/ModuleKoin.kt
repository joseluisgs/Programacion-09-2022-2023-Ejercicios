package dev.sbytmacke.vehiculositv.di

import dev.sbytmacke.vehiculositv.config.AppConfig
import dev.sbytmacke.vehiculositv.repositories.VehiculesRepository
import dev.sbytmacke.vehiculositv.repositories.VehiculesRepositoryImpl
import dev.sbytmacke.vehiculositv.services.database.VehiculeSqlDelightClient
import dev.sbytmacke.vehiculositv.services.storages.VehiculesStorage
import dev.sbytmacke.vehiculositv.services.storages.VehiculesStorageImpl
import dev.sbytmacke.vehiculositv.viewmodels.MainViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ModuleKoin = module {
    single { AppConfig() }
    single { VehiculeSqlDelightClient(get()) } // AppConfig
    single { VehiculesRepositoryImpl(get()) } // AppConfig

    single { VehiculeSqlDelightClient(get()) } // AppConfig
    single<VehiculesRepository>(named("VehiculesRepository")) { VehiculesRepositoryImpl(get()) } // VehiculeSqlDelightClient
    single<VehiculesStorage>(named("VehiculesStorage")) { VehiculesStorageImpl() }

    single {
        MainViewModel(
            get(named("VehiculesRepository")),
            get(named("VehiculesStorage"))
        )
    } // VehiculesRepository && VehiculesStorage
}
