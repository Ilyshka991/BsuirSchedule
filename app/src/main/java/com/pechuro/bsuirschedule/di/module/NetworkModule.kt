package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.remote.ApiConfig.BASE_URL
import com.pechuro.bsuirschedule.remote.ApiConfig.CONNECT_TIMEOUT
import com.pechuro.bsuirschedule.remote.api.*
import com.pechuro.bsuirschedule.remote.common.NetworkAvailabilityChecker
import com.pechuro.bsuirschedule.remote.interceptor.NetworkAvailabilityInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideRetrofit(
            httpClient: OkHttpClient,
            converterFactory: Converter.Factory
    ): Retrofit =
            Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(converterFactory)
                    .baseUrl(BASE_URL)
                    .build()

    @Provides
    @AppScope
    fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            networkAvailabilityInterceptor: NetworkAvailabilityInterceptor
    ): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(networkAvailabilityInterceptor)
                    .build()

    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { Logger.tag("Retrofit").i(it) })
        interceptor.level = BASIC
        return interceptor
    }

    @Provides
    @AppScope
    fun provideNetworkAvailabilityInterceptor(networkChecker: NetworkAvailabilityChecker): NetworkAvailabilityInterceptor =
            NetworkAvailabilityInterceptor(networkChecker)

    @Provides
    @AppScope
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @AppScope
    fun provideConverterFactory(moshi: Moshi): Converter.Factory =
            MoshiConverterFactory.create(moshi)

    @Provides
    @AppScope
    fun provideAnnouncementApi(retrofit: Retrofit): AnnouncementApi =
            retrofit.create(AnnouncementApi::class.java)

    @Provides
    @AppScope
    fun provideBuildingApi(retrofit: Retrofit): BuildingApi =
            retrofit.create(BuildingApi::class.java)

    @Provides
    @AppScope
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi =
            retrofit.create(ScheduleApi::class.java)

    @Provides
    @AppScope
    fun provideSpecialityApi(retrofit: Retrofit): SpecialityApi =
            retrofit.create(SpecialityApi::class.java)

    @Provides
    @AppScope
    fun provideStaffApi(retrofit: Retrofit): StaffApi =
            retrofit.create(StaffApi::class.java)
}