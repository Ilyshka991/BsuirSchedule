package com.pechuro.bsuirschedule

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.domain.common.Logger
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    protected lateinit var loggerTree: Logger.Tree

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        initLogger()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    protected open fun initDI() {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Logger.add(loggerTree)
        }
    }
}