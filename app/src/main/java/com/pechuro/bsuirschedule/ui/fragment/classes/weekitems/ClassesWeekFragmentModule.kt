package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems

import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.adapter.ClassesWeekAdapter
import dagger.Module
import dagger.Provides

@Module
class ClassesWeekFragmentModule {

    @Provides
    fun provideListAdapter() = ClassesWeekAdapter()
}
