package de.rhm.cityweather

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.BeforeClass
import org.junit.Test

class WeatherViewModelTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }
    }

    val observer = TestObserver<WeatherUiState>()
    val repository = mock<WeatherRepository>()
    val viewModel = WeatherViewModel(repository)

    @Test
    fun subscribeAfterFetchWeather_emitsCachedState() {
        whenever(repository.getWeather(any())).thenReturn(Single.error(Exception()))
        viewModel.fetchWeather.onNext("")
        viewModel.uiState.subscribe(observer)
        observer.values().run { assert(first() is WeatherUiState.Failure) {this} }
    }

}