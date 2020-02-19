package com.pechuro.bsuirschedule

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.work.*
import com.pechuro.bsuirschedule.di.component.AppComponent
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.domain.common.Logger
import com.pechuro.bsuirschedule.worker.UpdateScheduleWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class App : Application(), Configuration.Provider {

    @Inject
    protected lateinit var loggerTree: Logger.Tree
    @Inject
    protected lateinit var workerFactory: WorkerFactory

    lateinit var appComponent: AppComponent

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogger()
        startWorkers()
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

    private fun startWorkers() {
        val updateScheduleRequest = PeriodicWorkRequest.Builder(
                UpdateScheduleWorker::class.java,
                1, TimeUnit.DAYS
        )
                .addTag(UpdateScheduleWorker.TAG)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                UpdateScheduleWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                updateScheduleRequest
        )
    }
}