package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesDayFragmentProvider {

    @ContributesAndroidInjector(modules = [ClassesDayFragmentModule::class])
    fun provideListFragmentFactory(): ClassesDayFragment
}
