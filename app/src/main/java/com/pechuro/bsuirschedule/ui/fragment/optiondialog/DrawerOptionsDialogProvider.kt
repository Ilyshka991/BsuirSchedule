package com.pechuro.bsuirschedule.ui.fragment.optiondialog

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DrawerOptionsDialogProvider {

    @ContributesAndroidInjector()
    fun bind(): DrawerOptionsDialog
}
