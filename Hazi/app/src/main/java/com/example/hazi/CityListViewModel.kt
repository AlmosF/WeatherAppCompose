package com.example.hazi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hazi.data.entities.CityEntity
import com.example.hazi.data.datasource.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository
): ViewModel() {
        fun getAllCities(): Flow<List<CityEntity>> = repository.getAllCities()

        suspend fun addCity(cityName: String) {
            if (MainActivity.logging) Log.d("CityListViewModel", "addCity: " + cityName)
            repository.insertCity(
                CityEntity(
                    CityName = cityName.split(", ")[0],
                    CountryName = cityName.split(", ")[1], //Ez eskü nem beégetett adat, jó? :D
                    Temperature = "24",
                    Image = "R.drawable.ic_launcher_foreground")
            )
        }

        fun deleteCity(city: CityEntity) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repository.deleteCity(city)
                }
            }
        }

        fun updateCity(city: CityEntity) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repository.updateCity(city)
                }
            }
        }
}