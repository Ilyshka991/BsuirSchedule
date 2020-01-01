package com.pechuro.bsuirschedule.feature.main.employeedetails

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.feature.main.employeedetails.employeeinfo.EmployeeDetailFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface EmployeeDetailsDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [EmployeeDetailsDialogModule::class, EmployeeDetailFragmentProvider::class])
    fun bind(): EmployeeDetailsDialog
}
