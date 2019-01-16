package com.pechuro.bsuirschedule.di.module

import android.content.Context
import androidx.room.Room
import com.pechuro.bsuirschedule.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideScheduleDao(appDatabase: AppDatabase) = appDatabase.scheduleDao()

    @Provides
    @Singleton
    fun provideEmployeeDao(appDatabase: AppDatabase) = appDatabase.employeeDao()

    @Provides
    @Singleton
    fun provideGroupDao(appDatabase: AppDatabase) = appDatabase.groupDao()
}