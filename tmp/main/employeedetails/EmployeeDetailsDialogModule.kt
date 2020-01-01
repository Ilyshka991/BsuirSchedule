package com.pechuro.bsuirschedule.feature.main.employeedetails

import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class EmployeeDetailsDialogModule {

    @Provides
    @FragmentScope
    fun providePagerAdapter(fragment: EmployeeDetailsDialog) =
            EmployeeDetailsPagerAdapter(fragment.childFragmentManager)
}