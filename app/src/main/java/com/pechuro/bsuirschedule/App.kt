package com.pechuro.bsuirschedule

import android.app.Application
import android.arch.persistence.room.Room
import com.pechuro.bsuirschedule.repository.ScheduleRepository
import com.pechuro.bsuirschedule.repository.api.ScheduleApi
import com.pechuro.bsuirschedule.repository.db.AppDatabase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {

    //temp solution instead of Dagger
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var appDatabase: AppDatabase
        private lateinit var scheduleRepository: ScheduleRepository

        fun injectAppDatabase() = appDatabase

        fun injectScheduleRepository() = scheduleRepository

    }

    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://students.bsuir.by/api/v1/")
                .build()

        appDatabase = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").build()
        scheduleRepository = ScheduleRepository(retrofit.create(ScheduleApi::class.java), appDatabase.scheduleDao())
    }
}

