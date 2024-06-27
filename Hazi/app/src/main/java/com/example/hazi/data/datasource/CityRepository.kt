package com.example.hazi.data.datasource

import com.example.hazi.data.entities.CityEntity
import kotlinx.coroutines.flow.Flow


interface CityRepository {
    fun getAllCities(): Flow<List<CityEntity>>

    fun getCityById(id: Long): CityEntity

    suspend fun insertCity(city: CityEntity)

    suspend fun updateCity(city: CityEntity)

    suspend fun deleteCity(city: CityEntity)

}