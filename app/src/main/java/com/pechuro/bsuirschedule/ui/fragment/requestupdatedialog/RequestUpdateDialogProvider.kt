package com.pechuro.bsuirschedule.ui.fragment.requestupdatedialog

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface RequestUpdateDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): RequestUpdateDialog
}
