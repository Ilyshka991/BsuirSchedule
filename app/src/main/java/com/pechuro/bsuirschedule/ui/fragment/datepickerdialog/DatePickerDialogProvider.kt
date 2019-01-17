package com.pechuro.bsuirschedule.ui.fragment.datepickerdialog

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DatePickerDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): DatePickerDialog
}
