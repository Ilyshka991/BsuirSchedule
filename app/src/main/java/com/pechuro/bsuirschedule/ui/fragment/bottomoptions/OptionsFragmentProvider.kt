package com.pechuro.bsuirschedule.ui.fragment.bottomoptions

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface OptionsFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): BottomOptionsFragment
}
