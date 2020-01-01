package com.pechuro.bsuirschedule.feature.main.adddialog

import dagger.Module
import dagger.Provides

@Module
class AddDialogModule {

    @Provides
    fun providePagerAdapter(fragment: AddDialog) =
            AddDialogPagerAdapter(fragment.childFragmentManager)
}

