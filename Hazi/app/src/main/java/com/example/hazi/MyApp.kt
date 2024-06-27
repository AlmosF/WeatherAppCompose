package com.example.hazi

import android.app.Application
import com.example.hazi.data.AppDatabase
import com.example.hazi.data.dao.CityDao
import com.example.hazi.data.datasource.CityRepository
import com.example.hazi.data.datasource.CityRepositoryImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject



@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var repository: CityRepository
}