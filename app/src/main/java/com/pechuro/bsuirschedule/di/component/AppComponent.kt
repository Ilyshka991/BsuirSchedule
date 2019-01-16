package com.pechuro.bsuirschedule.di.component

import android.content.SharedPreferences
import com.google.gson.Gson
import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    AppActivitiesModule::class,
    AppViewModelsModule::class,
    NetworkModule::class,
    RxModule::class,
    SharedPrefsModule::class,
    DatabaseModule::class])
interface AppComponent {

    fun inject(app: App)

    fun getSharedPrefs(): SharedPreferences
    fun getGson(): Gson

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}