package com.pechuro.bsuirschedule.feature.main.start

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface StartFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): StartFragment
}
