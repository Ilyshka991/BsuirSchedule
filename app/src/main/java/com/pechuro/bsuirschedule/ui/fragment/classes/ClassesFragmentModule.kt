package com.pechuro.bsuirschedule.ui.fragment.classes

import dagger.Module
import dagger.Provides

@Module
class ClassesFragmentModule {

    @Provides
    fun providePagerAdapter(fragment: ClassesFragment) =
            ClassesPagerAdapter(fragment.childFragmentManager)
}