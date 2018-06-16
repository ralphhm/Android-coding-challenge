package de.rhm.cityweather

import android.app.Application
import org.koin.android.ext.android.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(listOf(AppModule))
    }
}