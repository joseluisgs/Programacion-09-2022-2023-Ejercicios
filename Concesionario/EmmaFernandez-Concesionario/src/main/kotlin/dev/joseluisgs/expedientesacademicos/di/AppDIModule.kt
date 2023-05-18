package dev.joseluisgs.expedientesacademicos.di

import dev.joseluisgs.expedientesacademicos.config.AppConfig
import dev.joseluisgs.expedientesacademicos.repositories.CochesRepository
import dev.joseluisgs.expedientesacademicos.repositories.CochesRepositoryImpl
import dev.joseluisgs.expedientesacademicos.services.database.SqlDeLightClient
import dev.joseluisgs.expedientesacademicos.services.storage.StorageCoches
import dev.joseluisgs.expedientesacademicos.services.storage.StorageCochesImpl
import dev.joseluisgs.expedientesacademicos.viewmodels.ConcesionarioViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AppDIModule = module {
    singleOf(::AppConfig)
    singleOf(::SqlDeLightClient)
    singleOf(::CochesRepositoryImpl) {
        bind<CochesRepository>()
    }
    singleOf(::StorageCochesImpl) {
        bind<StorageCoches>()
    }
    singleOf(::ConcesionarioViewModel)
}
