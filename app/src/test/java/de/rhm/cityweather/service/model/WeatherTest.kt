package de.rhm.cityweather.service.model

import com.squareup.moshi.Moshi
import de.rhm.cityweather.AppModule
import de.rhm.cityweather.service.ServiceModule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.AutoCloseKoinTest

private const val JSON_RESPONSE = """
{
    "coord": {
        "lon": -0.13,
        "lat": 51.51
    },
    "weather": [
        {
            "id": 803,
            "main": "Clouds",
            "description": "Überwiegend bewölkt",
            "icon": "04n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 17.49,
        "pressure": 1020,
        "humidity": 77,
        "temp_min": 16,
        "temp_max": 19
    },
    "visibility": 10000,
    "wind": {
        "speed": 6.2,
        "deg": 240
    },
    "clouds": {
        "all": 75
    },
    "dt": 1529360400,
    "sys": {
        "type": 1,
        "id": 5091,
        "message": 0.0038,
        "country": "GB",
        "sunrise": 1529293365,
        "sunset": 1529353262
    },
    "id": 2643743,
    "name": "London",
    "cod": 200
}
"""

class WeatherTest: AutoCloseKoinTest() {

    val moshi: Moshi by inject()

    @Before
    fun before(){
        startKoin(listOf(ServiceModule, AppModule))
    }

    @Test
    fun parse() = moshi.adapter(Weather::class.java).fromJson(JSON_RESPONSE)!!.run {
        assertEquals("London", name)
        assertEquals("17.49", details.temp)
    }

}