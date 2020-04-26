package com.pechuro.bsuirschedule

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.pechuro.bsuirschedule.common.AppAnalytics
import com.pechuro.bsuirschedule.di.component.AppComponent
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.worker.CheckScheduleUpdatesWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class App : Application(), Configuration.Provider, LifecycleObserver {

    @Inject
    protected lateinit var loggerTree: Logger.Tree

    @Inject
    protected lateinit var workerFactory: WorkerFactory

    @Inject
    protected lateinit var analyticsReporter: AppAnalytics.Reporter

    lateinit var appComponent: AppComponent

    val isInForeground: Boolean
        get() = ProcessLifecycleOwner.get()
                .lifecycle
                .currentState
                .isAtLeast(Lifecycle.State.STARTED)

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogger()
        initAnalytics()
        startCheckScheduleUpdatesWorker()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

    protected open fun initDI() {
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .also {
                    it.inject(this)
                }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Logger.add(loggerTree)
        }
    }

    private fun initAnalytics() {
        AppAnalytics.set(analyticsReporter)
    }

    private fun startCheckScheduleUpdatesWorker() {
        val workRequest = CheckScheduleUpdatesWorker.createPeriodicRequest(
                scheduleAtHour = 20,
                repeatIntervalMillis = TimeUnit.DAYS.toMillis(1)
        )
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                CheckScheduleUpdatesWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        )
    }
}