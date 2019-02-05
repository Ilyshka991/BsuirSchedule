package com.pechuro.bsuirschedule.ui.fragment.employeedetails

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.ui.fragment.employeedetails.employeeinfo.EmployeeDetailFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface EmployeeDetailsDialogProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [EmployeeDetailsDialogModule::class, EmployeeDetailFragmentProvider::class])
    fun bind(): EmployeeDetailsDialog
}
