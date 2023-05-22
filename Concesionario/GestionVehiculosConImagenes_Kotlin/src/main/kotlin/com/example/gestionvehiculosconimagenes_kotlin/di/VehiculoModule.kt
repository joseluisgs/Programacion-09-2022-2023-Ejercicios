package com.example.gestionvehiculosconimagenes_kotlin.di

import com.example.gestionvehiculosconimagenes_kotlin.config.ConfigApp
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.IVehiculoRepository
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.VehiculoRepositoryMySQL
import com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo.VehiculoRepositorySQLDelight
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseSQLDelight
import com.example.gestionvehiculosconimagenes_kotlin.service.database.DatabaseMySQL
import com.example.gestionvehiculosconimagenes_kotlin.service.storage.IStorageImages
import com.example.gestionvehiculosconimagenes_kotlin.service.storage.StorageImagesImpl
import com.example.gestionvehiculosconimagenes_kotlin.viewmodel.ViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vehiculoModule = module {
    single { ConfigApp() }

    single { DatabaseSQLDelight(get()) }

    single<IVehiculoRepository>(named("SQLD")){ VehiculoRepositorySQLDelight(get()) }

    single { DatabaseMySQL(get()) }

    single<IVehiculoRepository>(named("MySQL")){ VehiculoRepositoryMySQL(get()) }

    single<IStorageImages> { StorageImagesImpl(get()) }

    single { ViewModel(get(named("MySQL")), get()) }
}