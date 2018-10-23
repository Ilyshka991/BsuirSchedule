package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.di.annotations.ActivityScope
import com.pechuro.bsuirschedule.ui.activity.editlesson.EditLessonActivity
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivityModule
import com.pechuro.bsuirschedule.ui.activity.settings.SettingsActivity
import com.pechuro.bsuirschedule.ui.activity.splash.SplashActivity
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogProvider
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.bottomsheet.OptionsFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.ClassesDayFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.ClassesWeekFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        NavigationActivityModule::class,
        ClassesFragmentProvider::class,
        ClassesDayFragmentProvider::class,
        ClassesWeekFragmentProvider::class,
        ExamFragmentProvider::class,
        StartFragmentProvider::class,
        AddDialogProvider::class,
        AddFragmentProvider::class,
        OptionsFragmentProvider::class])
    abstract fun bindNavigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindInfoLoadActivity(): InfoLoadActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindSettingsActivity(): SettingsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindEditLessonActivity(): EditLessonActivity
}