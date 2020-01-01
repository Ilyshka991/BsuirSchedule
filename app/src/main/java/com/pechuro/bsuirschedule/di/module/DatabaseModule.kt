package com.pechuro.bsuirschedule.di.module

import android.content.Context
import androidx.room.Room
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.local.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @AppScope
    fun provideAppDatabase(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @AppScope
    fun provideScheduleDao(appDatabase: AppDatabase) = appDatabase.scheduleDao()

    @Provides
    @AppScope
    fun provideEmployeeDao(appDatabase: AppDatabase) = appDatabase.employeeDao()

    @Provides
    @AppScope
    fun provideGroupDao(appDatabase: AppDatabase) = appDatabase.groupDao()
}