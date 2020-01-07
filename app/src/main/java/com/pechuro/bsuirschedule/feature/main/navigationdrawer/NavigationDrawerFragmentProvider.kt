package com.pechuro.bsuirschedule.feature.main.navigationdrawer

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface NavigationDrawerFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    fun bind(): NavigationDrawerFragment
}
