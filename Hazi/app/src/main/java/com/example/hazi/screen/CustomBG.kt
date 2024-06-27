package com.example.hazi.screen

import android.util.Log
import com.example.hazi.MainActivity
import com.example.hazi.R
import com.example.hazi.model.WeatherData
import com.example.hazi.model.WeatherResult
import java.util.Date

fun CustomBG (weatherResult: WeatherResult) : Int {

    val current = weatherResult.weather[0].main.lowercase()
    val currentTime = Date(System.currentTimeMillis())

    if (MainActivity.logging) Log.d("TIME", currentTime.toString())

    val day = weatherResult.weather[0].icon.contains("d")

    when (current) {
        "thunderstorm" -> {
            return if(day) R.mipmap.thunderstorm else R.mipmap.thunderstormnight
        }
        "drizzle" -> {
            return if(day) R.mipmap.drizzle else R.mipmap.drizzlenight
        }
        "rain" -> {
            return if(day) R.mipmap.rain else R.mipmap.rainnight
        }
        "snow" -> {
            return if(day) R.mipmap.snow else R.mipmap.snownight
        }
        "clear" -> {
            return if(day) R.mipmap.clear else R.mipmap.clearnight
        }
        "clouds" -> {
            return if(day) R.mipmap.clouds else R.mipmap.cloudsnight
        }
        "mist" -> {
            return if(day) R.mipmap.mist else R.mipmap.mistnight
        }
        "tornado" -> {
            return if(day) R.mipmap.tornado else R.mipmap.tornadonight
        }
        "smoke" -> {
            return if(day) R.mipmap.smoke else R.mipmap.smokenight
        }
        "haze" -> {
            return if(day) R.mipmap.haze else R.mipmap.hazenight
        }
        "fog" -> {
            return if(day) R.mipmap.fog else R.mipmap.fog
        }
        "squall" -> {
            return if(day) R.mipmap.squall else R.mipmap.squall
        }
        else -> {
            return if(day) R.mipmap.clear else R.mipmap.clearnight
        }
    }

}

fun CustomBGByWeatherData (weatherData: WeatherData?) : Int {

    val current = weatherData?.weather?.get(0)?.main?.lowercase()
    val currentTime = Date(System.currentTimeMillis())

    if (MainActivity.logging) Log.d("TIME", currentTime.toString())

    val day = weatherData?.weather?.get(0)?.icon?.contains("d")

    if (current == null || day == null) {
        return R.mipmap.clear
    }

    when (current) {
        "thunderstorm" -> {
            return if(day == true) R.mipmap.thunderstorm else R.mipmap.thunderstormnight
        }
        "drizzle" -> {
            return if(day == true) R.mipmap.rainp else R.mipmap.rainpn
        }
        "rain" -> {
            return if(day == true) R.mipmap.rainp else R.mipmap.rainpn
        }
        "snow" -> {
            return if(day == true) R.mipmap.snowingp else R.mipmap.snowingpn
        }
        "clear" -> {
            return if(day == true) R.mipmap.clearskyp else R.mipmap.clearskypn
        }
        "clouds" -> {
            return if(day == true) R.mipmap.cloudyp else R.mipmap.cloudypn
        }
        "mist" -> {
            return if(day == true) R.mipmap.mist else R.mipmap.mistnight
        }
        "tornado" -> {
            return if(day == true) R.mipmap.tornado else R.mipmap.tornadonight
        }
        "smoke" -> {
            return if(day == true) R.mipmap.smoke else R.mipmap.smokenight
        }
        "haze" -> {
            return if(day == true) R.mipmap.haze else R.mipmap.hazenight
        }
        "fog" -> {
            return if(day == true) R.mipmap.fog else R.mipmap.fog
        }
        "squall" -> {
            return if(day == true) R.mipmap.squall else R.mipmap.squall
        }
        else -> {
            return if(day == true) R.mipmap.clearskyp else R.mipmap.clearskypn
        }
    }

}

fun CustomBGByWeatherResult (weatherData: WeatherResult?) : Int {

    val current = weatherData?.weather?.get(0)?.main?.lowercase()
    val currentTime = Date(System.currentTimeMillis())

    if (MainActivity.logging) Log.d("TIME", currentTime.toString())

    val day = weatherData?.weather?.get(0)?.icon?.contains("d")

    if (current == null || day == null) {
        return R.mipmap.clear
    }

    when (current) {
        "thunderstorm" -> {
            return if(day == true) R.mipmap.thunderstorm else R.mipmap.thunderstormnight
        }
        "drizzle" -> {
            return if(day == true) R.mipmap.rainp else R.mipmap.rainpn
        }
        "rain" -> {
            return if(day == true) R.mipmap.rainp else R.mipmap.rainpn
        }
        "snow" -> {
            return if(day == true) R.mipmap.snowingp else R.mipmap.snowingpn
        }
        "clear" -> {
            return if(day == true) R.mipmap.clearskyp else R.mipmap.clearskypn
        }
        "clouds" -> {
            return if(day == true) R.mipmap.cloudyp else R.mipmap.cloudypn
        }
        "mist" -> {
            return if(day == true) R.mipmap.mist else R.mipmap.mistnight
        }
        "tornado" -> {
            return if(day == true) R.mipmap.tornado else R.mipmap.tornadonight
        }
        "smoke" -> {
            return if(day == true) R.mipmap.smoke else R.mipmap.smokenight
        }
        "haze" -> {
            return if(day == true) R.mipmap.haze else R.mipmap.hazenight
        }
        "fog" -> {
            return if(day == true) R.mipmap.fog else R.mipmap.fog
        }
        "squall" -> {
            return if(day == true) R.mipmap.squall else R.mipmap.squall
        }
        else -> {
            return if(day == true) R.mipmap.clearskyp else R.mipmap.clearskypn
        }
    }

}