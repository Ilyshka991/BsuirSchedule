package com.pechuro.bsuirschedule.ui.fragment.classes

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClassesFragmentProvider {

    @ContributesAndroidInjector(modules = [ClassesFragmentModule::class])
    fun bind(): ClassesFragment
}
