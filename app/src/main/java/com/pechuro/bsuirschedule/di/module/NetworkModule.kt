package com.pechuro.bsuirschedule.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pechuro.bsuirschedule.data.network.EmployeeApi
import com.pechuro.bsuirschedule.data.network.GroupApi
import com.pechuro.bsuirschedule.data.network.ScheduleApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {
    companion object {
        private const val BASE_URL = "https://students.bsuir.by/api/v1/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
            GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi =
            retrofit.create(ScheduleApi::class.java)

    @Provides
    @Singleton
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi =
            retrofit.create(EmployeeApi::class.java)

    @Provides
    @Singleton
    fun provideGroupApi(retrofit: Retrofit): GroupApi =
            retrofit.create(GroupApi::class.java)
}