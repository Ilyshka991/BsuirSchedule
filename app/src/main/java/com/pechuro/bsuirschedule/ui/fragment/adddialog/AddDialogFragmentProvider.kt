package com.pechuro.bsuirschedule.ui.fragment.adddialog

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDialogFragmentProvider {

    @ContributesAndroidInjector(modules = [AddDialogFragmentModule::class])
    fun bind(): AddDialogFragment
}
