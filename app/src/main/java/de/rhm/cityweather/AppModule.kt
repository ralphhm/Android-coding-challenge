package de.rhm.cityweather

import android.speech.SpeechRecognizer
import de.rhm.cityweather.speech.Speech
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.dsl.module.applicationContext

val AppModule = applicationContext {

    bean { AndroidSchedulers.mainThread() }

    bean { WeatherRepository(get()) }

    bean { SpeechRecognizer.createSpeechRecognizer(get()) }

    bean { Speech(get()) }

    factory { WeatherViewModel(get(), get(), get()) }

}