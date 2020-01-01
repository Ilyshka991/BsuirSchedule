package com.pechuro.bsuirschedule.feature.main.datepickerdialog

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DatePickerDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector()
    fun bind(): DatePickerDialog
}
