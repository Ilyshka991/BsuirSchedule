package com.pechuro.bsuirschedule.feature.add.addschedule

import dagger.Module
import dagger.Provides

@Module
class AddScheduleContainerModule {

    @Provides
    fun providePagerAdapter(fragment: AddScheduleContainer) =
            AddScheduleContainerPagerAdapter(fragment.childFragmentManager)
}

