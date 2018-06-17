package de.rhm.cityweather

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.rhm.cityweather.service.model.Weather
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.BeforeClass
import org.junit.Test

class ActionToStateTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }
    }

    val call = mock<(String) -> Single<Weather>>()
    val observer = TestObserver.create<WeatherUiState>()
    val action: Subject<String> = PublishSubject.create()

    init {
        ActionToState(call).apply(action).subscribe(observer)
    }

    @Test
    fun emitsLoadingState() {
        whenever(call.invoke(any())).thenReturn(Single.never())
        action.onNext("")
        observer.values().run {
            assert(first() is WeatherUiState.Loading)
        }
    }

    @Test
    fun emitsFailureState_afterError() {
        val exception = Exception()
        whenever(call.invoke(any())).thenReturn(Single.error(exception))
        action.onNext("")
        observer.values().run {
            assert(first() is WeatherUiState.Loading)
            assert(get(1) is WeatherUiState.Failure)
        }
    }

    @Test
    fun emitsResultState_afterSuccess() {
        whenever(call.invoke(any())).thenReturn(Single.just(mock()))
        action.onNext("")
        observer.values().run {
            assert(first() is WeatherUiState.Loading)
            assert(get(1) is WeatherUiState.Result)
        }
    }
}