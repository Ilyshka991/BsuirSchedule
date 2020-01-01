package com.pechuro.bsuirschedule.feature.main.draweroptions

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DrawerOptionsDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): DrawerOptionsDialog
}
