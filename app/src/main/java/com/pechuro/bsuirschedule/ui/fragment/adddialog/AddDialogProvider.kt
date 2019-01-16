package com.pechuro.bsuirschedule.ui.fragment.adddialog

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.ui.fragment.adddialog.addfragment.AddFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AddDialogModule::class, AddFragmentProvider::class])
    fun bind(): AddDialog
}
