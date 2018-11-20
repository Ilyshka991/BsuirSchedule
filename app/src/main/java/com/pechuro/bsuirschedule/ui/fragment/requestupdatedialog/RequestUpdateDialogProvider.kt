package com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface RequestUpdateDialogProvider {

    @ContributesAndroidInjector()
    fun bind(): RequestUpdateDialog
}
