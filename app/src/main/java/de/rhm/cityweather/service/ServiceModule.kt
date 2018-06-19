package de.rhm.cityweather.service

import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val ServiceModule = applicationContext {

    bean { Moshi.Builder().build() as Moshi }

    bean { MoshiConverterFactory.create(get()) as Converter.Factory }

    bean { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) as Interceptor }

    bean { OkHttpClient.Builder().addInterceptor(get()).build() as OkHttpClient }

    bean {
        Retrofit.Builder()
                .addConverterFactory(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl("http://api.openweathermap.org/")
                .client(get())
                .build()
                .create(OpenWeatherMapService::class.java) as OpenWeatherMapService
    }

}