package de.rhm.cityweather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import de.rhm.cityweather.service.model.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_weather.*
class WeatherActivity : AppCompatActivity() {

    private val repository = WeatherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        RxSearchView.queryTextChangeEvents(search_view)
                .bindToLifecycle(this)
                .filter { it.isSubmitted }
                .doOnNext { it.view().clearFocus() }
                .map { it.queryText().toString() }
                .switchMap { repository.getWeather(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ bind(it) }
    }

    private fun bind(weather: Weather) {
        Snackbar.make(toolbar, weather.toString(), Snackbar.LENGTH_LONG).show()
    }

}
