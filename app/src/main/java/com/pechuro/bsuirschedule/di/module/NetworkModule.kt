package com.pechuro.bsuirschedule.di.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pechuro.bsuirschedule.BuildConfig
import com.pechuro.bsuirschedule.data.network.EmployeeApi
import com.pechuro.bsuirschedule.data.network.GroupApi
import com.pechuro.bsuirschedule.data.network.ScheduleApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        private const val BASE_URL = "https://journal.bsuir.by/api/v1/"
        private const val CONNECT_TIMEOUT = 60L
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient,
                        gsonConverterFactory: GsonConverterFactory): Retrofit =
            Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { Log.i("Retrofit ", it) })
        if (BuildConfig.DEBUG) {
            interceptor.level = BODY
        }
        return interceptor
    }

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