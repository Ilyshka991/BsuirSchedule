package com.pechuro.bsuirschedule.ui.fragment.drawer

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DrawerFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [DrawerFragmentModule::class])
    fun bind(): DrawerFragment
}
