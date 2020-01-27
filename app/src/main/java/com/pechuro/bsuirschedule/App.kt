package com.pechuro.bsuirschedule

import android.app.Activity
import android.app.Application
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.domain.common.Logger
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {

    @Inject
    protected lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    protected lateinit var loggerTree: Logger.Tree

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogger()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Logger.add(loggerTree)
        }
    }
}