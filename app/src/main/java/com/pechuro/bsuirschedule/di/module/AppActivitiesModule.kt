package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.di.annotations.ActivityScope
import com.pechuro.bsuirschedule.feature.edit.EditLessonActivity
import com.pechuro.bsuirschedule.feature.edit.editlesson.EditLessonFragmentProvider
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivity
import com.pechuro.bsuirschedule.feature.main.MainActivity
import com.pechuro.bsuirschedule.feature.settings.SettingsActivity
import com.pechuro.bsuirschedule.feature.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
interface AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector
    fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun bindInfoLoadActivity(): InfoLoadActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun bindSettingsActivity(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        EditLessonFragmentProvider::class
    ])
    fun bindEditLessonActivity(): EditLessonActivity
}