package com.pechuro.bsuirschedule.feature.add.addschedule

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.feature.add.addschedule.fragment.AddScheduleFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddScheduleContainerProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        AddScheduleFragmentProvider::class,
        AddScheduleContainerModule::class
    ])
    fun bind(): AddScheduleContainer
}
