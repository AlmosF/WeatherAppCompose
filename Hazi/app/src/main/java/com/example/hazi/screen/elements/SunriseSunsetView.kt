package com.example.hazi.screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hazi.R
import com.example.hazi.model.WeatherResult
import com.example.hazi.ui.theme.blueBlack
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


@Composable
fun sun_Rise_Set(weatherResult: WeatherResult){
    Box (
        modifier = Modifier.height(200.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, blueBlack, RoundedCornerShape(16.dp))
                .background(blueBlack.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TableCell(
                        text = stringResource(R.string.sunrise),
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                    TableCell(
                        text = stringResource(R.string.sunset),
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val sign = if(weatherResult.timezone >= 0) "+" else "-"
                    val absTimezone = abs(weatherResult.timezone / 3600)
                    val timeZone = TimeZone.getTimeZone("GMT$sign$absTimezone")
                    sdf.timeZone = timeZone
                    TableCell(
                        text = sdf.format(Date(weatherResult.sys.sunrise*1000)),
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                    TableCell(
                        text = sdf.format(Date(weatherResult.sys.sunset *1000)),
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TableCell(
                        text = "Second row, first column",
                        coilImage = null,
                        sunriseset = R.mipmap.ic_sunrise,
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                    TableCell(
                        text = "Second row, second column",
                        coilImage = null,
                        sunriseset = R.mipmap.ic_sunset,
                        weight = 0.5f,
                        modifier = Modifier.weight(0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


