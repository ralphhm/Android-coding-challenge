package de.rhm.cityweather

import de.rhm.cityweather.service.model.Weather
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WeatherRepository {

    fun getWeather(city: String): Single<Weather> = Single.just(Weather(city, Weather.Main("26.1", "minTemp", "maxTemp"))).delay(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io())

}