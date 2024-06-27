package com.example.hazi.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResult(
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
    val cod: Long
)

@Serializable
data class Rain(
    val `1h`: Long
)

@Serializable
data class Clouds(
    val all: Long
)

@Serializable
data class Coord2(
    val lon: Double,
    val lat: Double
)

@Serializable
data class Main(
    val temp: Double,

    //@SerialName("feels_like")
    val feels_like: Double,

    //@SerialName("temp_min")
    val temp_min: Double,

   //@SerialName("temp_max")
    val temp_max: Double,

    val pressure: Long,
    val humidity: Long,
    val sea_level: Long,
    val grnd_level: Long
)

@Serializable
data class Sys2(
    val type: Long,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

@Serializable
data class Weather2(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class Wind2(
    val speed: Double,
    val deg: Long,
    val gust: Double
)
