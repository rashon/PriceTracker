package com.example.pricetracker

import android.app.Application
import com.example.pricetracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class PriceTrackerApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PriceTrackerApp)
            modules(appModule)
        }
    }
}