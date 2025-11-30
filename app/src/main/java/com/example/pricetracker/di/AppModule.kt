package com.example.pricetracker.di

import org.koin.core.module.Module

val appModule: List<Module> = listOf(
    dataModule,
    presentationModule
)