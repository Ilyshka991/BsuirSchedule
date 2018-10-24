package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesItemFragmentProvider {

    @ContributesAndroidInjector(modules = [ClassesItemFragmentModule::class])
    fun provideListFragmentFactory(): ClassesItemFragment
}
