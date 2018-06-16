package de.rhm.cityweather

import de.rhm.cityweather.service.model.Weather
import io.reactivex.Observable

class WeatherRepository {

    fun getWeather(city: String): Observable<Weather> = Observable.just(Weather(city, Weather.Main("Temp", "minTemp", "maxTemp")))

}