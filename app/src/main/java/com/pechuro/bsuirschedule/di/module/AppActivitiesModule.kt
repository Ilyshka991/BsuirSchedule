package com.pechuro.bsuirschedule.di.module

import com.pechuro.bsuirschedule.di.annotations.ActivityScope
import com.pechuro.bsuirschedule.ui.activity.infoload.InfoLoadActivity
import com.pechuro.bsuirschedule.ui.activity.navigation.NavigationActivity
import com.pechuro.bsuirschedule.ui.activity.splash.SplashActivity
import com.pechuro.bsuirschedule.ui.fragment.adddialog.AddDialogProvider
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.classes.ClassesFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.list.ListFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.start.StartFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
abstract class AppActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ClassesFragmentProvider::class,
        ListFragmentProvider::class,
        StartFragmentProvider::class,
        AddDialogProvider::class,
        AddFragmentProvider::class])
    abstract fun bindNavigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindInfoLoadActivity(): InfoLoadActivity
}