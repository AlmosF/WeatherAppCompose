package com.example.hazi.screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.WeatherResult
import com.example.hazi.ui.theme.blueBlack
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

@Composable
fun forecastView(
    weatherResult: WeatherResult,
    forecastResult: ForecastResponse,
){
    val column1Weight = .10f
    val column2Weight = .45f
    var unit = if(LocalContext.current.getSharedPreferences("sharedPref", 0).getBoolean("unit", true)){
        "°C"
    } else {
        "°F"
    }
    val tableData = (1..7).mapIndexed { index, item ->
        index to "Item $index"
    }
    LazyColumn(
        Modifier
            .padding(16.dp)
            .border(2.dp, blueBlack, RoundedCornerShape(16.dp))
            .background(blueBlack.copy(0.4f), RoundedCornerShape(16.dp))
            .height(250.dp)
    ) {

        items(tableData.size) { index ->
            val item = tableData[index]


            val sdf = SimpleDateFormat("E, HH:mm", Locale.getDefault())
            val sign = if(weatherResult.timezone >= 0) "+" else "-"
            val absTimezone = abs(weatherResult.timezone / 3600)
            val timeZone = TimeZone.getTimeZone("GMT$sign$absTimezone")
            sdf.timeZone = timeZone

            Row (
                modifier = Modifier.fillMaxHeight()
            ){
                TableCell(
                    text = item.first.toString(),
                    weight = column1Weight,
                    //coilImage = "https://openweathermap.org/img/wn/${forecastResult.list[index].weather[0].icon}.png"
                    coilImage = forecastResult.list[index].weather[0].icon
                )

                TableCell(
                    text = String.format("%.1f", forecastResult.list[index].main.temp_min) + unit + " / " +
                            String.format("%.1f", forecastResult.list[index].main.temp_max) + unit,
                    weight = column2Weight,
                    textAlign = TextAlign.Center
                )

                TableCell(
                    text = sdf.format(forecastResult.list[index].dt * 1000),
                    weight = column2Weight,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}