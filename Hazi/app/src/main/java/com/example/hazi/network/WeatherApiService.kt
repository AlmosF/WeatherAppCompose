package com.example.hazi.network

import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.WeatherResult
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val SERVICE_URL = "https://api.openweathermap.org"


val gson = GsonBuilder().setLenient().create()

private val retrofit = Retrofit.Builder()
    .baseUrl(SERVICE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

object WAPI {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

interface WeatherApiService {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(@Query("q") cityName: String,
                               @Query("units") units: String,
                               @Query("appid") appId: String): WeatherResult

    @GET("/data/2.5/forecast")
    suspend fun getForecastData(@Query("q") cityName: String,
                                @Query("units") units: String,
                                @Query("cnt") cnt: Int?,
                                @Query("appid") appId: String): ForecastResponse
}