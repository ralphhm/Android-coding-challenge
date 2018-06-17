package de.rhm.cityweather

import android.arch.lifecycle.ViewModel
import de.rhm.cityweather.service.model.Weather
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class WeatherViewModel(weatherRepository: WeatherRepository): ViewModel() {

    val fetchWeather = PublishSubject.create<String>()
    val uiState: Observable<out WeatherUiState> = fetchWeather
            .compose(ActionToState {weatherRepository.getWeather(it)})
            .startWith(WeatherUiState.Initial)
            .replay(1)
            .autoConnect(0)
            .observeOn(AndroidSchedulers.mainThread())

}

sealed class WeatherUiState {
    object Initial: WeatherUiState()
    object Loading: WeatherUiState()
    class Result(val weather: Weather): WeatherUiState()
    object Failure: WeatherUiState()
}

class ActionToState(private inline val call: (String) -> Single<Weather>) : ObservableTransformer<String, WeatherUiState> {

    override fun apply(upstream: Observable<String>): ObservableSource<WeatherUiState> {
        return upstream.switchMap { action ->
            call.invoke(action)
                    //map success case
                    .map<WeatherUiState> { WeatherUiState.Result(it) }
                    .toObservable()
                    //emit loading state while fetching
                    .startWith(WeatherUiState.Loading)
                    //emit failure state if fetch failed providing an action that triggers another fetch
                    .onErrorReturn { WeatherUiState.Failure }
        }
    }

}