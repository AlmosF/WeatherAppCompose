package com.example.hazi.screen

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hazi.R

enum class WindDirection(val degrees: Float) {
    N(0f),
    NNE(22.5f),
    NE(45f),
    ENE(67.5f),
    E(90f),
    ESE(112.5f),
    SE(135f),
    SSE(157.5f),
    S(180f),
    SSW(202.5f),
    SW(225f),
    WSW(247.5f),
    W(270f),
    WNW(292.5f),
    NW(315f),
    NNW(337.5f);

    companion object {
        @Composable
        fun getDirectionString(degrees: Float): String {
            val windDirStringMap = mapOf(
                "N" to R.string.N,
                "NNE" to R.string.NNE,
                "NE" to R.string.NE,
                "ENE" to R.string.ENE,
                "E" to R.string.E,
                "ESE" to R.string.ESE,
                "SE" to R.string.SE,
                "SSE" to R.string.SSE,
                "S" to R.string.S,
                "SSW" to R.string.SSW,
                "SW" to R.string.SW,
                "WSW" to R.string.WSW,
                "W" to R.string.W,
                "WNW" to R.string.WNW,
                "NW" to R.string.NW,
                "NNW" to R.string.NNW
            )
            val adjustedDegrees = degrees % 360.0
            val direction = entries.firstOrNull { it.degrees.toDouble() in (adjustedDegrees - 22.4)..(adjustedDegrees + 22.4) }
            return direction?.let { stringResource(windDirStringMap[it.name]!!) } ?: ""
        }
    }
}

fun forgatas(legnyomas: Double, context : Context): Triple<Double, Color, String> {
    if (legnyomas < 1000.0) {
        return Triple(238.0, Color.Yellow, context.getString(R.string.low_pressure))
    }
    else if(legnyomas > 1028.0){
        return Triple(122.0, Color.Red, context.getString(R.string.high_pressure))
    }

    return when (legnyomas) {
        in 1000.0..1014.0 -> {
            (318-238)/(1014.0-1000.0)*legnyomas+238
            Triple(linearInterpolation(legnyomas, 1000.0, 1014.0, 238.0, 318.0), Color.Yellow, context.getString(R.string.low_pressure))
        }
        in 1015.0..1020.0 -> {
            (360+42-319)/(1020.0-1015.0)*legnyomas+319
            Triple(linearInterpolation(legnyomas, 1015.0, 1020.0, 319.0, 360.0+42.0), Color.Green,
                context.getString(
                    R.string.normal_pressure
                ))
        }
        in 1021.0 .. 1028.0 -> {
            (122-43)/(1028.0-1021.0)*legnyomas+43
            Triple(linearInterpolation(legnyomas, 1021.0, 1028.0, 43.0, 122.0), Color.Red, context.getString(R.string.high_pressure))
        }
        else -> Triple(0.0, Color.Black, "")
    }
}

fun linearInterpolation(x: Double, x1: Double, x2: Double, y1: Double, y2: Double): Double {
    return (y2 - y1) / (x2 - x1) * (x - x1) + y1
}