package com.pechuro.bsuirschedule

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pechuro.bsuirschedule.di.component.AppComponent
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.domain.common.Logger
import javax.inject.Inject

open class App : Application() {

    @Inject
    protected lateinit var loggerTree: Logger.Tree

    lateinit var appComponent: AppComponent

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogger()
    }

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
}