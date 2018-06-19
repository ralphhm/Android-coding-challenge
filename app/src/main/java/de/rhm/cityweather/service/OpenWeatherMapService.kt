package de.rhm.cityweather.service

import de.rhm.cityweather.service.model.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("data/2.5/weather?units=metric")
    fun getCityWeather(@Query("q") searchQuery: String, @Query("APPID") apiKey: String): Single<Weather>

}