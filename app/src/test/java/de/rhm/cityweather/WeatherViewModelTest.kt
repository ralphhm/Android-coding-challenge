package de.rhm.cityweather

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class WeatherViewModelTest {

    val observer = TestObserver<WeatherUiState>()
    val repository = mock<WeatherRepository>()
    val viewModel = WeatherViewModel(repository, mock(), Schedulers.trampoline())

    @Test
    fun subscribeAfterFetchWeather_emitsCachedState() {
        whenever(repository.getWeather(any())).thenReturn(Single.error(Exception()))
        viewModel.fetchWeather.onNext("")
        viewModel.uiState.subscribe(observer)
        observer.values().run { assert(first() is WeatherUiState.Failure) {this} }
    }

}