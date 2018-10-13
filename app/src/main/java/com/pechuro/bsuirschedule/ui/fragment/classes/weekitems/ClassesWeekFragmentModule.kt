package com.pechuro.bsuirschedule.ui.fragment.classes.weekitems

import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.ui.fragment.classes.weekitems.adapter.ClassesWeekAdapter
import dagger.Module
import dagger.Provides

@Module
class ClassesWeekFragmentModule {

    @Provides
    fun provideLinearLayoutManager(fragment: ClassesWeekFragment) =
            LinearLayoutManager(fragment.context)

    @Provides
    fun provideListAdapter() = ClassesWeekAdapter()
}
