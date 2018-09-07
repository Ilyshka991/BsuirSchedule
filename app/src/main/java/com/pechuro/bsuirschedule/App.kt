package com.pechuro.bsuirschedule

import android.app.Application
import android.arch.persistence.room.Room
import com.pechuro.bsuirschedule.repository.EmployeeRepository
import com.pechuro.bsuirschedule.repository.api.EmployeeApi
import com.pechuro.bsuirschedule.repository.db.AppDatabase
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {

    //temp solution instead of Dagger
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var employeeApi: EmployeeApi
        private lateinit var employeeRepository: EmployeeRepository
        private lateinit var appDatabase: AppDatabase

        fun injectEmployeeApi() = employeeApi

        fun injectGroupRepository() = employeeRepository

        fun injectEmployeeDao() = appDatabase.employeeDao()
    }

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://students.bsuir.by/api/")
                .build()

        employeeApi = retrofit.create(EmployeeApi::class.java)
        appDatabase = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").build()

        employeeRepository = EmployeeRepository(employeeApi, appDatabase.employeeDao())
    }
}
