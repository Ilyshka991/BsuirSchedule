package com.pechuro.bsuirschedule

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    protected open fun initDI() {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }
}