package com.example.hazi.model

import kotlinx.serialization.Serializable


@Serializable
data class ForecastWeatherResult(
    val cod: Long,
    val coord: Coord2,
    val weather: List<Weather2>,
    val base: String,
    val main: Main,
    val visibility: Long,
    val wind: Wind2,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys2,
    val timezone: Long,
    val id: Long,
    val name: String,
    val dt_txt: String
)

@Serializable
data class ForecastResponse(
    val coord : Coord2,
    val list: List<ForecastWeatherResult>,
    val base: String,
    val timezone: Long,
    val id: Long,
    val name : String,
    val cod: Long
)