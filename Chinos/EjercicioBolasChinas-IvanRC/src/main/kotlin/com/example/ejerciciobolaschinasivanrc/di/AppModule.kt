package com.example.ejerciciobolaschinasivanrc.di

import com.example.ejerciciobolaschinasivanrc.viewmodel.ViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val diModule = module {
    singleOf(::ViewModel)
}