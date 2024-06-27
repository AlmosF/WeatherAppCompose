package com.example.hazi.screen.elements

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hazi.R
import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.WeatherResult
import com.example.hazi.ui.theme.blueBlack

@Composable
fun details(
    weatherResult: WeatherResult,
    forecastResult: ForecastResponse
){
    var unit = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)

    val tableDetailsData = (1..7).mapIndexed { index, item ->
        index to "Item $index"
    }
    val column1Weight = .1f
    val column2Weight = .9f
    val tableSunData = (1..3).mapIndexed { index, item ->
        index to "Item $index"
    }
    val column1SunWeight = .5f
    val column2SunWeight = .5f


    val iconList = listOf(
        R.mipmap.ic_barometer,
        R.mipmap.ic_humidity,
        R.mipmap.ic_wind,
        R.mipmap.ic_winddir,
        R.mipmap.ic_sealevel,
        R.mipmap.ic_groundlevel,
        R.mipmap.ic_cloud
    )

    val weatherList = listOf(
        weatherResult.main.pressure,
        weatherResult.main.humidity,
        weatherResult.wind.speed,
        weatherResult.wind.deg,
        weatherResult.main.sea_level,
        weatherResult.main.grnd_level,
        weatherResult.clouds.all
    )

    val details_units = listOf(
        "hPa",
        "%",
        if(unit) "m/s" else "mi/h",
        "°",
        "hPa",
        "hPa",
        "%"
    )


    Column (
        modifier = Modifier.height(285.dp)
    ) {
        Box() {
            LazyColumn(
                Modifier
                    .padding(16.dp)
                    .border(2.dp, blueBlack, RoundedCornerShape(16.dp))
                    .background(blueBlack.copy(0.4f), RoundedCornerShape(16.dp))
            ) {
                items(tableDetailsData.size) { index ->
                    val item = tableDetailsData[index]
                    Row {
                        TableCell(
                            text = item.first.toString(),
                            weight = column1Weight,
                            icon = iconList[index],
                            contentAlignment = Alignment.CenterStart
                        )

                        TableCell(
                            text = weatherList[index].toString()+details_units[index],
                            weight = column2Weight,
                            contentAlignment = Alignment.CenterStart
                        )
                    }
                }
            }
        }
    }
}