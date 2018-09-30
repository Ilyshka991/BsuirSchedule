package com.pechuro.bsuirschedule.ui.fragment.list

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ListFragmentProvider {

    @ContributesAndroidInjector(modules = [ListFragmentModule::class])
    fun provideListFragmentFactory(): ListFragment
}
