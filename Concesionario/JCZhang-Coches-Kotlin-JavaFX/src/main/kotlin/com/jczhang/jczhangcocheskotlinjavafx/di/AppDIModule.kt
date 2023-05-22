package com.jczhang.jczhangcocheskotlinjavafx.di

import com.jczhang.jczhangcocheskotlinjavafx.config.AppConfig
import com.jczhang.jczhangcocheskotlinjavafx.repositories.coches.CrudRepositoryImpl
import com.jczhang.jczhangcocheskotlinjavafx.services.database.DatabaseService
import com.jczhang.jczhangcocheskotlinjavafx.viewmodel.CochesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val AppDIModule = module {
    singleOf(::AppConfig)
    singleOf(::DatabaseService)
    singleOf(::CrudRepositoryImpl)
    singleOf(::CochesViewModel)
}