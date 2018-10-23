package com.pechuro.bsuirschedule.ui.fragment.bottomsheet

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface OptionsFragmentProvider {

    @ContributesAndroidInjector()
    fun bind(): ScheduleOptionsFragment
}
