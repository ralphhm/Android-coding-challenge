package de.rhm.cityweather

import org.koin.dsl.module.applicationContext

val AppModule = applicationContext {

    bean { WeatherRepository() }

    factory { WeatherViewModel(get()) }

}