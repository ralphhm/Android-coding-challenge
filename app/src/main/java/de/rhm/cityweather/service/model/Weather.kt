package de.rhm.cityweather.service.model

data class Weather(val name: String, val details: Main) {
    data class Main(val temp: String, val minTemp: String, val maxTemp: String)
}