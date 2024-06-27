package com.example.hazi.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hazi.MainActivity
import com.example.hazi.network.APODAPI
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface APODUiState {
    data class Success(val apodResult: APODResult) : APODUiState
    data class Error(val error: String) : APODUiState
    object Loading : APODUiState
}

class APODViewModel : ViewModel(){
    var apodUiState: APODUiState by mutableStateOf(APODUiState.Loading)
        private set

    init {
        if (MainActivity.logging) Log.d("APODViewModel", "init")
        getAPOD()
    }

    fun getAPOD(date: String = "") {
        if (MainActivity.logging) Log.d("APODViewModel", "getAPOD")

        viewModelScope.launch {
            apodUiState = try {
                val result = if (date.isEmpty()) {
                    APODAPI.retrofitService.getAPODData("wAQ9mfnR4lyrNoIBJMzh9KYIyvx4saGcKQrrpFQB")
                } else {
                    APODAPI.retrofitService.getAPODData("wAQ9mfnR4lyrNoIBJMzh9KYIyvx4saGcKQrrpFQB", date)
                }
                APODUiState.Success(result)
            } catch (e: HttpException) {
                APODUiState.Error("HttpException: ${e.message()}")
            } catch (e: IOException) {
                APODUiState.Error("IOException: ${e.message}")
            }
        }
    }
}