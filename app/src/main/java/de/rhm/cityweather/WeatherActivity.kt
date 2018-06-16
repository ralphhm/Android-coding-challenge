package de.rhm.cityweather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import de.rhm.cityweather.WeatherUiState.*
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.android.ext.android.inject

class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        viewModel.uiState.bindToLifecycle(this).subscribe{bind(it)}
        RxSearchView.queryTextChangeEvents(search_view)
                .bindToLifecycle(this)
                .filter { it.isSubmitted }
                .doOnNext { it.view().clearFocus() }
                .map { it.queryText().toString() }
                .subscribe{ viewModel.fetchWeather.onNext(it) }
    }

    private fun bind(uiState: WeatherUiState) = when(uiState) {
        is Initial -> info.setText(R.string.welcome)
        is Loading -> info.text = "Loading"
        is Result -> info.text = uiState.weather.toString()
        is Failure -> info.text = "Failure"
    }

}
