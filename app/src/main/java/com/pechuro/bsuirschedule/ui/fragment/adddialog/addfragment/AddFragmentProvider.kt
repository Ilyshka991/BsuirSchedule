package com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddFragmentProvider {

    @ContributesAndroidInjector()
    fun bind(): AddFragment
}
