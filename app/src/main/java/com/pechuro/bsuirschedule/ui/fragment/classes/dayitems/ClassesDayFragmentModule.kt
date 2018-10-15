package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems

import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.adapter.ClassesDayAdapter
import dagger.Module
import dagger.Provides

@Module
class ClassesDayFragmentModule {
    @Provides
    fun provideListAdapter() = ClassesDayAdapter()
}
