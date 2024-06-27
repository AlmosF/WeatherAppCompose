package com.example.hazi.data.di

import android.content.Context
import androidx.room.Room
import com.example.hazi.data.AppDatabase
import com.example.hazi.data.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Ez nem is létezett

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Provides
    @Singleton
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "city_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCityDao(
        db: AppDatabase
    ): CityDao = db.cityDao()


}