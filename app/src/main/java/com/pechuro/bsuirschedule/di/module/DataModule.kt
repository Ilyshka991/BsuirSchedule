package com.pechuro.bsuirschedule.di.module

import android.content.Context
import android.content.SharedPreferences
import com.pechuro.bsuirschedule.common.provider.AppUriProvider
import com.pechuro.bsuirschedule.common.provider.AppUriProviderImpl
import com.pechuro.bsuirschedule.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @AppScope
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @AppScope
    fun provideUriProvider(context: Context): AppUriProvider = AppUriProviderImpl(context = context)
}