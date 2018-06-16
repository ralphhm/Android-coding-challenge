package de.rhm.cityweather

import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.dryRun

class KoinTest : AutoCloseKoinTest() {

    @Test
    fun dryRunTest() {
        // start Koin
        startKoin(listOf(AppModule))
        // dry run of given module list
        dryRun()
    }

}