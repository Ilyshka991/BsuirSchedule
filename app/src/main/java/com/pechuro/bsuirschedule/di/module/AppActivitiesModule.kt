package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.di.annotations.ActivityScope
import com.pechuro.bsuirschedule.feature.edit.EditLessonActivity
import com.pechuro.bsuirschedule.feature.edit.editlesson.EditLessonFragmentProvider
import com.pechuro.bsuirschedule.feature.load.InfoLoadActivity
import com.pechuro.bsuirschedule.feature.main.MainActivity
import com.pechuro.bsuirschedule.feature.main.addschedule.AddScheduleDialogProvider
import com.pechuro.bsuirschedule.feature.main.navigationdrawer.NavigationDrawerFragmentProvider
import com.pechuro.bsuirschedule.feature.main.start.StartFragmentProvider
import com.pechuro.bsuirschedule.feature.settings.SettingsActivity
import com.pechuro.bsuirschedule.feature.splash.SplashActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module(includes = [AndroidInjectionModule::class])
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
    @ContributesAndroidInjector(modules = [
        NavigationDrawerFragmentProvider::class,
        AddScheduleDialogProvider::class,
        StartFragmentProvider::class
    ])
    fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        EditLessonFragmentProvider::class
    ])
    fun bindEditLessonActivity(): EditLessonActivity
}