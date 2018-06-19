package de.rhm.cityweather

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import de.rhm.cityweather.WeatherUiState.*
import de.rhm.cityweather.speech.SpeechState
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.info.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.weather.*
import org.koin.android.architecture.ext.viewModel

private const val REQUEST_PERMISSION_AUDIO_REC = 1

class WeatherActivity : AppCompatActivity() {

    private val viewModel by viewModel<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        viewModel.uiState.bindToLifecycle(this).subscribe { bind(it) }
        viewModel.speechState.bindToLifecycle(this).subscribe { bind(it) }
        RxSearchView.queryTextChangeEvents(search_view)
                .bindToLifecycle(this)
                .filter { it.isSubmitted }
                .doOnNext { it.view().clearFocus() }
                .map { it.queryText().toString() }
                .subscribe { viewModel.fetchWeather.onNext(it) }
        RxView.clicks(actionSpeak)
                .bindToLifecycle(this)
                .subscribe { viewModel.speak.onNext(it) }
    }

    private fun bind(uiState: WeatherUiState) = when (uiState) {
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

    private fun bind(speechState: SpeechState) = when (speechState) {
        is SpeechState.Listening -> actionSpeak.setColorFilter(Color.GREEN)
        is SpeechState.NeedsPermission -> {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Snackbar.make(toolbar, "Record Audio permission is needed to use voice recognition", Snackbar.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION_AUDIO_REC)
        }
        is SpeechState.Failure -> {
            actionSpeak.setColorFilter(Color.RED)
            Snackbar.make(toolbar, "Error while voice recognition: ${speechState.message}", Snackbar.LENGTH_LONG).show()
        }
        is SpeechState.Result -> {
            actionSpeak.setColorFilter(Color.WHITE)
            search_view.setQuery(speechState.text, true)
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
