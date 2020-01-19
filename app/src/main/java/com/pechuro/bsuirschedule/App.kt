package com.pechuro.bsuirschedule

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }


}