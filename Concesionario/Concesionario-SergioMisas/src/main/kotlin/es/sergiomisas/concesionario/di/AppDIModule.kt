package es.sergiomisas.concesionario.di

import es.sergiomisas.concesionario.config.AppConfig
import es.sergiomisas.concesionario.repositories.CocheRepository
import es.sergiomisas.concesionario.repositories.CocheRepositoryImpl
import es.sergiomisas.concesionario.services.database.SqlDeLightClient
import es.sergiomisas.concesionario.services.storage.StorageCoches
import es.sergiomisas.concesionario.services.storage.StorageCochesImpl
import es.sergiomisas.concesionario.viewmodels.ConcesionarioViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AppDIModule = module {
    singleOf(::AppConfig)
    singleOf(::SqlDeLightClient)
    singleOf(::CocheRepositoryImpl) {
        bind<CocheRepository>()
    }
    singleOf(::StorageCochesImpl) {
        bind<StorageCoches>()
    }
    singleOf(::ConcesionarioViewModel)
}
