package com.pechuro.bsuirschedule

import android.app.Application
import com.pechuro.bsuirschedule.di.component.AppComponent
import com.pechuro.bsuirschedule.di.component.DaggerAppComponent
import com.pechuro.bsuirschedule.di.module.AppModule


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}

