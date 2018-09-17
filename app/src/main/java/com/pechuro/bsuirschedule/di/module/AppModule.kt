package com.pechuro.bsuirschedule.di.module

import android.content.Context
import com.pechuro.bsuirschedule.App
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: App) {
    @Provides
    fun provideContext(): Context = app
}