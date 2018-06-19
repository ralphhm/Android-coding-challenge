package de.rhm.cityweather.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(@Json(name = "name") val name: String, @Json(name = "main") val details: Main) {
    @JsonClass(generateAdapter = true)
    data class Main(@Json(name = "temp") val temp: String)
}