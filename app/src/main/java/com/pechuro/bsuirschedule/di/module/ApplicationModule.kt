package com.pechuro.bsuirschedule.di.module

import android.appwidget.AppWidgetManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.common.AndroidLoggerTree
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.common.FlurryAnalyticsReporter
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
    fun provideAnalyticsReporter(crashlytics: FirebaseCrashlytics): AppAnalytics.Reporter =
        FlurryAnalyticsReporter(crashlytics)

    @Provides
    fun provideWidgetManager(context: Context): AppWidgetManager =
        AppWidgetManager.getInstance(context)

    @Provides
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    fun provideFirebaseCrashlytics() = FirebaseCrashlytics.getInstance()

}