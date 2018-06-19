package de.rhm.cityweather

import android.speech.SpeechRecognizer
import com.nhaarman.mockito_kotlin.mock
import de.rhm.cityweather.service.ServiceModule
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.koin.android.ext.koin.with
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.dryRun

class KoinTest : AutoCloseKoinTest() {

    val TestModule = applicationContext {

        bean { Schedulers.trampoline() }
        bean { mock<SpeechRecognizer>() }
    }

    @Test
    fun dryRunTest() {
        // start Koin
        startKoin(listOf(ServiceModule, AppModule, TestModule)) with mock()
        // dry run of given module list
        dryRun()
    }

}