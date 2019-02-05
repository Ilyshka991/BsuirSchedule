package com.pechuro.bsuirschedule.ui.fragment.employeedetails.employeeinfo

import com.pechuro.bsuirschedule.di.annotations.ChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface EmployeeDetailFragmentProvider {

    @ChildFragmentScope
    @ContributesAndroidInjector()
    fun bind(): EmployeeDetailFragment
}
