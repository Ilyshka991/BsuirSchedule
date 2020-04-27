package com.pechuro.bsuirschedule.di.module

import android.content.Context
import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.AndroidLoggerTree
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.AppAnalyticsReporter
import com.pechuro.bsuirschedule.common.NetworkAvailabilityCheckerImpl
import com.pechuro.bsuirschedule.di.annotations.AppScope
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.remote.common.NetworkAvailabilityChecker
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @AppScope
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @AppScope
    fun provideNetworkAvailabilityChecker(context: Context): NetworkAvailabilityChecker =
            NetworkAvailabilityCheckerImpl(context)

    @Provides
    @AppScope
    fun provideLoggerTree(): Logger.Tree = AndroidLoggerTree()

    @Provides
    @AppScope
    fun provideAnalyticsReporter(): AppAnalytics.Reporter = AppAnalyticsReporter()
}