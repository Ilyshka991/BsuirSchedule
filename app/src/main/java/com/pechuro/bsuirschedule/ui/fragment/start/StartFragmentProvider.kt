package com.pechuro.bsuirschedule.ui.fragment.start

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface StartFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): StartFragment
}
