package com.pechuro.bsuirschedule.di.component

import com.pechuro.bsuirschedule.MainActivity
import com.pechuro.bsuirschedule.di.module.AppModule
import com.pechuro.bsuirschedule.di.module.DatabaseModule
import com.pechuro.bsuirschedule.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class, DatabaseModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}