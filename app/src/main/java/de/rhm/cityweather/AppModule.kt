package de.rhm.cityweather

import de.rhm.cityweather.speech.Speech
import org.koin.dsl.module.applicationContext

val AppModule = applicationContext {

    bean { WeatherRepository() }

    bean { Speech(get()) }

    factory { WeatherViewModel(get(), get()) }

}