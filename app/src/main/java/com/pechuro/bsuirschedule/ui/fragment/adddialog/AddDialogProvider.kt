package com.pechuro.bsuirschedule.ui.fragment.adddialog

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AddDialogProvider {

    @ContributesAndroidInjector(modules = [AddDialogModule::class])
    fun bind(): AddDialog
}
