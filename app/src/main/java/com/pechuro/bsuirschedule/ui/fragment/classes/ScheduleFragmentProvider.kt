package com.pechuro.bsuirschedule.ui.fragment.classes

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ScheduleFragmentProvider {

    @ContributesAndroidInjector(modules = [ScheduleFragmentModule::class])
    fun bind(): ScheduleFragment
}
