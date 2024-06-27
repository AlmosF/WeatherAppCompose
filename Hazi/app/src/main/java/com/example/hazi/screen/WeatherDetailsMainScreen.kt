package com.example.hazi.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.WeatherResult
import com.example.hazi.model.Wind
import com.example.hazi.screen.elements.BaroMeter
import com.example.hazi.screen.elements.Wind
import com.example.hazi.screen.elements.forecastView

@Composable
fun WeatherDetailsMainScreen(
    weatherResult: WeatherResult,
    forecastResult: ForecastResponse
){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        forecastView(weatherResult = weatherResult, forecastResult = forecastResult)
        Wind(Wind(speed = weatherResult.wind.speed.toFloat(), deg = weatherResult.wind.deg.toFloat(), gust = weatherResult.wind.gust.toFloat()))
        BaroMeter(pressure = weatherResult.main.pressure.toDouble())
    }
}