package com.pechuro.bsuirschedule.ui.fragment.adddialog

import dagger.Module
import dagger.Provides

@Module
class AddDialogModule {
    @Provides
    fun providePagerAdapter(fragment: AddDialog) =
            AddDialogPagerAdapter(fragment.childFragmentManager)
}

