package com.example.hazi.screen.test

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hazi.MainActivity
import com.example.hazi.R
import com.example.hazi.model.WeatherUiState
import com.example.hazi.model.WeatherViewModel2
import com.example.hazi.screen.elements.CardBox2

@Composable
fun WeatherCardP(
    weatherViewModel2: WeatherViewModel2 = viewModel(),
    navController: NavController,
    removeItem: () -> Unit,
    local: Boolean = true,
    modifier : Modifier = Modifier
) {

    when(weatherViewModel2.weatherUiState) {
        is WeatherUiState.Loading -> {
            if (MainActivity.logging) Log.d("WeatherCardP", "WeatherCardP: Loading..")
            CardBox2(
                cityItem = "Loading..",
                removeItem = { removeItem() },
                navController = navController,
                cardBgPic = R.mipmap.clouds,
                weatherData = null
            )
        }
        is WeatherUiState.Success  -> {
            if (MainActivity.logging) Log.d("WeatherCardP", "WeatherCardP: Success..")
            CardBox2(
                cityItem = (weatherViewModel2.weatherUiState as WeatherUiState.Success).weatherResult.name,
                removeItem = { removeItem() },
                navController = navController,
                cardBgPic = R.mipmap.clouds,
                weatherData = (weatherViewModel2.weatherUiState as WeatherUiState.Success).weatherResult,
                localCity = local
            )
        }
        is WeatherUiState.Error -> {
            CardBox2(
                cityItem = "Error..",
                removeItem = { removeItem() },
                navController = navController,
                cardBgPic = R.mipmap.clouds,
                weatherData = null
            )
        }
    }

}