package com.pechuro.bsuirschedule.ui.fragment.classes

import dagger.Module
import dagger.Provides

@Module
class ScheduleFragmentModule {

    @Provides
    fun providePagerAdapter(fragment: ScheduleFragment) =
            SchedulePagerAdapter(fragment.requireFragmentManager())
}