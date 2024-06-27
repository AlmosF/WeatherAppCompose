package com.example.hazi.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hazi.data.entities.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun getAllCityItems(): Flow<List<CityEntity>>

    @Query("SELECT * FROM city WHERE CityId = :id")
    fun getCityItemById(id: Long): CityEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCityItem(city: CityEntity)

    @Delete
    suspend fun deleteCityItem(city: CityEntity)

    @Update
    suspend fun updateCityItem(city: CityEntity)

}