package com.example.loschinos.di

import com.example.loschinos.viewmodels.ChinosViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AppDIModule = module {
    singleOf(::ChinosViewModel)
}