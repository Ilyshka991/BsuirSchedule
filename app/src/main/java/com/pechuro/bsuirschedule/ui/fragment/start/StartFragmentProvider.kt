package com.pechuro.bsuirschedule.ui.fragment.start

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface StartFragmentProvider {

    @ContributesAndroidInjector()
    fun bind(): StartFragment
}
