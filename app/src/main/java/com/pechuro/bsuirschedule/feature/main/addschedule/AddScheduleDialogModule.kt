package com.pechuro.bsuirschedule.feature.main.addschedule

import dagger.Module
import dagger.Provides

@Module
class AddScheduleDialogModule {

    @Provides
    fun providePagerAdapter(fragment: AddScheduleDialog) =
            AddScheduleDialogPagerAdapter(fragment.childFragmentManager)
}

