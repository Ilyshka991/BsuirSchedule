package com.pechuro.bsuirschedule.ui.fragment.drawer

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DrawerFragmentProvider {

    @ContributesAndroidInjector(modules = [DrawerFragmentModule::class])
    fun bind(): DrawerFragment
}
