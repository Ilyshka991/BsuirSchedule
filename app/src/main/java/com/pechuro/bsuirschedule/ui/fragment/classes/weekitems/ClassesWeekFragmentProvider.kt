package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesWeekFragmentProvider {

    @ContributesAndroidInjector(modules = [ClassesWeekFragmentModule::class])
    fun provideListFragmentFactory(): ClassesWeekFragment
}
