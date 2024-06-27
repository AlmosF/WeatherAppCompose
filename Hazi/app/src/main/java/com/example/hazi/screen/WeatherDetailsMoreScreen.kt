package com.example.hazi.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.WeatherResult
import com.example.hazi.screen.elements.Humidity
import com.example.hazi.screen.elements.Visibility
import com.example.hazi.screen.elements.details
import com.example.hazi.screen.elements.sun_Rise_Set


@Composable
fun WeatherDetailsMoreScreen(
    weatherResult: WeatherResult,
    forecastResult: ForecastResponse
){
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        details(weatherResult = weatherResult, forecastResult = forecastResult)
        sun_Rise_Set(weatherResult = weatherResult)
        Row {
            Visibility(modifier = Modifier.weight(1f), visRang = weatherResult.visibility.toDouble(), context = LocalContext.current)
            Humidity(humidity = weatherResult.main.humidity.toInt(), modifier = Modifier.weight(1f))
        }
    }
}