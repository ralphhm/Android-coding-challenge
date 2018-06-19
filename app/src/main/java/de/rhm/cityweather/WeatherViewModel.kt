package de.rhm.cityweather

import android.arch.lifecycle.ViewModel
import de.rhm.cityweather.service.model.Weather
import de.rhm.cityweather.speech.Speech
import de.rhm.cityweather.speech.SpeechState
import io.reactivex.*
import io.reactivex.subjects.PublishSubject

class WeatherViewModel(weatherRepository: WeatherRepository, speech: Speech, scheduler: Scheduler): ViewModel() {

    val fetchWeather = PublishSubject.create<String>()
    val uiState: Observable<out WeatherUiState> = fetchWeather
            .compose(ActionToState {weatherRepository.getWeather(it)})
            .startWith(WeatherUiState.Initial)
            .replay(1)
            .autoConnect(0)
            .observeOn(scheduler)

    val speak = PublishSubject.create<Any>()
    val speechState: Observable<out SpeechState> = speak
            .concatMap { speech.observable }
            .replay(1)
            .autoConnect(0)
            .observeOn(scheduler)
}

sealed class WeatherUiState {
    object Initial: WeatherUiState()
    class Loading(val cityName: String): WeatherUiState()
    class Result(val weather: Weather): WeatherUiState()
    class Failure(val cityName: String): WeatherUiState()
}

class ActionToState(private inline val call: (String) -> Single<Weather>) : ObservableTransformer<String, WeatherUiState> {

    override fun apply(upstream: Observable<String>): ObservableSource<WeatherUiState> {
        return upstream.switchMap { cityName ->
            call.invoke(cityName)
                    //map success case
                    .map<WeatherUiState> { WeatherUiState.Result(it) }
                    .toObservable()
                    //emit loading state while fetching
                    .startWith(WeatherUiState.Loading(cityName))
                    //emit failure state if fetch failed
                    .onErrorReturn { WeatherUiState.Failure(cityName) }
        }
    }

}