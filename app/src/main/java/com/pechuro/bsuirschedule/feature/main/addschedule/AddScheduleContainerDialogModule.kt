package com.pechuro.bsuirschedule.feature.main.addschedule

import dagger.Module
import dagger.Provides

@Module
class AddScheduleContainerDialogModule {

    @Provides
    fun providePagerAdapter(fragment: AddScheduleContainerDialog) =
            AddScheduleContainerDialogPagerAdapter(fragment.childFragmentManager)
}

