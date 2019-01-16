package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import com.pechuro.bsuirschedule.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesItemFragmentProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = [ClassesItemFragmentModule::class])
    fun provideListFragmentFactory(): ClassesItemFragment
}
