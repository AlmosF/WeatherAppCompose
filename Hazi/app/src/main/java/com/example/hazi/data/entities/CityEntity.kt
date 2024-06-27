package com.example.hazi.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val CityId: Int = 0,
    @ColumnInfo(name = "CityName") var CityName: String,
    @ColumnInfo(name = "CountryName") var CountryName: String,
    @ColumnInfo(name = "Temperature") var Temperature: String,
    @ColumnInfo(name = "Image") var Image: String,
)