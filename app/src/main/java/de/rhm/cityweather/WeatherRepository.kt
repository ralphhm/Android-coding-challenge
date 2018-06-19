package de.rhm.cityweather

import de.rhm.cityweather.BuildConfig.OPEN_WEATHER_MAP_API_KEY
import de.rhm.cityweather.service.OpenWeatherMapService
import de.rhm.cityweather.service.model.Weather
import io.reactivex.Single

class WeatherRepository(val service: OpenWeatherMapService) {

    fun getWeather(city: String): Single<Weather> = service.getCityWeather(city, OPEN_WEATHER_MAP_API_KEY)

}