package com.example.hazi.data.geoCoding

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hazi.MainActivity
import com.example.hazi.network.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



class GeocodingViewModelFactory(private val latLong: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeocodingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GeocodingViewModel(latLong) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface GeocodingUiState {
    data class Success(val geocodingResult: GeocodingResponse) : GeocodingUiState
    data class Error(val error: String) : GeocodingUiState
    object Loading : GeocodingUiState
}


class GeocodingViewModel(latLong: String) : ViewModel() {
    private val _geocodingData = mutableStateOf<GeocodingResponse?>(null)
    var geocodingUiState: GeocodingUiState by mutableStateOf(GeocodingUiState.Loading)
        private set

    init {
        val latLongSplit = latLong.split(",")
        fetchGeocidingData(lat = latLongSplit[0].toDouble(), lon = latLongSplit[1].toDouble())
    }

    fun fetchGeocidingData(lat: Double, lon: Double){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                geocodingUiState = try {
                    if (MainActivity.logging) Log.d("FETCH", "IN try")

                    val result = GAPI.retrofitService.getReverseGeoCoding(
                        lat, lon, 1, "962b62fa651821cb2f5e36697d365bb0"
                    )
                    GeocodingUiState.Success(result[0])
                }catch (e: Exception) {
                    if (MainActivity.logging) Log.d("FETCH", "IN ERROR ${e.message}")
                    GeocodingUiState.Error("Error ${e.message}")
                }catch (e: retrofit2.HttpException){
                    if (MainActivity.logging) Log.d("FETCH", "IN ERROR ${e.message}")
                    GeocodingUiState.Error("Error ${e.message}")
                }
            }
        }
    }
}
private const val SERVICE_URL = "https://api.openweathermap.org"

private val retrofit = Retrofit.Builder()
    .baseUrl(SERVICE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

object GAPI {
    val retrofitService : GeocodingApiService by lazy{
        retrofit.create(GeocodingApiService::class.java)
    }
}

interface GeocodingApiService{
    @GET("/geo/1.0/reverse")
    suspend fun getReverseGeoCoding(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") appId: String
    ) : List<GeocodingResponse>
}