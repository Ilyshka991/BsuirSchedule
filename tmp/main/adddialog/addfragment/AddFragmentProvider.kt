package com.pechuro.bsuirschedule.feature.main.adddialog.addfragment

import com.pechuro.bsuirschedule.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddFragmentProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector()
    fun bind(): AddFragment
}
