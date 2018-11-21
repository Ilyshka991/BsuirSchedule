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
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.ClassesItemFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.drawer.DrawerFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.draweroptions.DrawerOptionsDialogProvider
import com.pechuro.bsuirschedule.ui.fragment.edit.EditLessonFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.exam.ExamFragmentProvider
import com.pechuro.bsuirschedule.ui.fragment.itemoptions.ItemOptionsDialogProvider
import com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog.RequestUpdateDialogProvider
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
        ClassesItemFragmentProvider::class,
        ExamFragmentProvider::class,
        StartFragmentProvider::class,
        AddDialogProvider::class,
        AddFragmentProvider::class,
        ItemOptionsDialogProvider::class,
        DrawerOptionsDialogProvider::class,
        DrawerFragmentProvider::class,
        RequestUpdateDialogProvider::class,
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
    @ContributesAndroidInjector(modules = [
        EditLessonFragmentProvider::class
    ])
    abstract fun bindEditLessonActivity(): EditLessonActivity
}