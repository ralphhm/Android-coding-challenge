package de.rhm.cityweather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import de.rhm.cityweather.WeatherUiState.*
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.info.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.weather.*
import org.koin.android.architecture.ext.viewModel

class WeatherActivity : AppCompatActivity() {

    private val viewModel by viewModel<WeatherViewModel>()

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
        is Initial -> infoText.run {
            contentGroup.onlyShow(this)
            setText(R.string.welcome)
        }
        is Loading -> loadingGroup.run {
            contentGroup.onlyShow(this)
            loadingInfoText.text = getString(R.string.loading_city_weather, uiState.cityName)
        }
        is Result -> weatherGroup.run {
            contentGroup.onlyShow(this)
            nameText.text = uiState.weather.name
            tempText.text = getString(R.string.temperature, uiState.weather.details.temp)
        }
        is Failure -> infoText.run {
            contentGroup.onlyShow(this)
            text = context.getString(R.string.error_loading_weather, uiState.cityName)
        }
    }

}

fun ViewGroup.onlyShow(view: View) = forEach { child -> child.visibility = if (child == view) VISIBLE else GONE }

/** Performs the given action on each view in this view group. */
inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}
