package com.example.hazi.network

import retrofit2.http.Query
import com.example.hazi.model.APODResult
import retrofit2.http.GET

private const val SERVICE_URL = "https://api.nasa.gov"

private val retrofit = retrofit2.Retrofit.Builder()
    .baseUrl(SERVICE_URL)
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

object APODAPI {
    val retrofitService: APODApiService by lazy {
        retrofit.create(APODApiService::class.java)
    }
}

interface APODApiService {
    @GET("/planetary/apod")
    suspend fun getAPODData(
        @Query("api_key") apiKey: String,
        @Query("date") date: String? = null
    ): APODResult
}