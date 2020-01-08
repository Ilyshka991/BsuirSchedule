package com.pechuro.bsuirschedule.feature.main.addschedule

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.feature.main.addschedule.fragment.AddScheduleFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddScheduleDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        AddScheduleFragmentProvider::class,
        AddScheduleDialogModule::class
    ])
    fun bind(): AddScheduleDialog
}
