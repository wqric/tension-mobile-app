package com.example.tension.common

import android.app.Application
import com.example.tension.common.ModuleDI
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class App(): Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(ModuleDI().module)
        }
    }
}