package com.example.hazi.model


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hazi.MainActivity
import com.example.hazi.network.WAPI
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModelFactory(private val cityName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel2::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel2(cityName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


sealed interface WeatherUiState {
    data class Success(val weatherResult: WeatherResult) : WeatherUiState
    data class Error(val error: String) : WeatherUiState
    object Loading : WeatherUiState
}

class WeatherViewModel2(cityName: String) : ViewModel(){
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    init {
        if (MainActivity.logging) Log.d("WeatherViewModel2", "init: $cityName")
        getWeather(cityName)
    }

    private fun getWeather(cityName: String) {
        val ApiKey = cityName.split(",")[2]
        val bool = cityName.split(",")[1].toBoolean()
        val cityName = cityName.split(",")[0]
        val unit = if(bool) "metric" else "imperial"

        if (MainActivity.logging) Log.d("WeatherViewModel2", "getWeather: $cityName $ApiKey $unit")

        if (ApiKey.isEmpty()){
            weatherUiState = WeatherUiState.Error("API Key is empty")
            return
        }

        viewModelScope.launch {
            weatherUiState = try {
                val result = WAPI.retrofitService.getWeatherData(
                    cityName, unit, ApiKey
                )
                WeatherUiState.Success(result)
            } catch (e: IOException) {
                WeatherUiState.Error("${e.message}")
            } catch (e: HttpException) {
                WeatherUiState.Error("${e.message}")
            }
        }
    }
}

class WeatherViewModel(savedStateHandle: SavedStateHandle) : ViewModel(){

    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    init {
        if (MainActivity.logging) Log.d("WeatherViewModel", "init: ")
        savedStateHandle.get<String>("cityName")?.let {
            if (MainActivity.logging) Log.d("WeatherViewModell", "init: $it")
            getWeather(it)
        }
    }

    private fun getWeather(cityName: String) {
        val ApiKey = cityName.split(",")[2]
        val bool = cityName.split(",")[1].toBoolean()
        val cityName = cityName.split(",")[0]
        val unit = if(bool) "metric" else "imperial"

        if (ApiKey.isEmpty()){
            weatherUiState = WeatherUiState.Error("API Key is empty")
            return
        }

        viewModelScope.launch {
            weatherUiState = try {
                val result = WAPI.retrofitService.getWeatherData(
                    cityName, unit, ApiKey
                )
                WeatherUiState.Success(result)
            } catch (e: IOException) {
                WeatherUiState.Error("${e.message}")
            } catch (e: HttpException) {
                WeatherUiState.Error("${e.message}")
            }
        }
    }
}



sealed interface ForecastUiState {
    data class Success(val forecastResult: ForecastResponse) : ForecastUiState
    data class Error(val error: String) : ForecastUiState
    object Loading : ForecastUiState
}

class ForecastViewModel(savedStateHandle: SavedStateHandle) : ViewModel(){

    var forecastUiState: ForecastUiState by mutableStateOf(ForecastUiState.Loading)
        private set

    init {
        savedStateHandle.get<String>("cityName")?.let {
            if (MainActivity.logging) Log.d("FWeatherViewModel", "init: $it")
            getForecast(it)
        }
    }

    private fun getForecast(cityName: String) {
        val ApiKey = cityName.split(",")[2]
        val bool = cityName.split(",")[1].toBoolean()
        val cityName = cityName.split(",")[0]
        val unit = if(bool) "metric" else "imperial"

        if (ApiKey.isEmpty()){
            forecastUiState = ForecastUiState.Error("API Key is empty")
            return
        }

        viewModelScope.launch {
            forecastUiState = try {
                val result = WAPI.retrofitService.getForecastData(
                    cityName,unit,7,ApiKey
                )
                ForecastUiState.Success(result)
            } catch (e: IOException) {
                ForecastUiState.Error("${e.message}")
            } catch (e: HttpException) {
                ForecastUiState.Error("${e.message}")
            }
        }
    }
}

