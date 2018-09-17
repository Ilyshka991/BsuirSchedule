package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.MainActivity
import com.pechuro.bsuirschedule.di.annotations.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector()
    fun mainActivityInjector(): MainActivity

}