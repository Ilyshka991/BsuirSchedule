package com.pechuro.bsuirschedule.ui.fragment.itemoptions

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ItemOptionsDialogProvider {

    @ContributesAndroidInjector()
    fun bind(): ItemOptionsDialog
}
