package com.example.hazi.data.datasource

import com.example.hazi.data.entities.CityEntity
import com.example.hazi.data.dao.CityDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Ez sem létezett az egész



class CityRepositoryImpl @Inject constructor(
    private val dao: CityDao
): CityRepository {

    override fun getAllCities(): Flow<List<CityEntity>> = dao.getAllCityItems()

    override fun getCityById(id: Long): CityEntity = dao.getCityItemById(id)

    override suspend fun insertCity(city: CityEntity) { dao.insertCityItem(city) }

    override suspend fun updateCity(city: CityEntity) { dao.updateCityItem(city) }

    override suspend fun deleteCity(city: CityEntity) { dao.deleteCityItem(city) }
}