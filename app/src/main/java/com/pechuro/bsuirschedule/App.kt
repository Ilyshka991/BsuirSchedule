package com.pechuro.bsuirschedule

import android.app.Application
import android.arch.persistence.room.Room
import com.pechuro.bsuirschedule.repository.GroupRepository
import com.pechuro.bsuirschedule.repository.api.GroupApi
import com.pechuro.bsuirschedule.repository.db.AppDatabase
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {

    //temp solution instead of Dagger
    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var groupApi: GroupApi
        private lateinit var groupRepository: GroupRepository
        private lateinit var appDatabase: AppDatabase

        fun injectUserApi() = groupApi

        fun injectGroupRepository() = groupRepository

        fun injectUserDao() = appDatabase.groupDao()
    }

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://students.bsuir.by/api/")
                .build()

        groupApi = retrofit.create(GroupApi::class.java)
        appDatabase = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").build()

        groupRepository = GroupRepository(groupApi, appDatabase.groupDao())
    }
}
