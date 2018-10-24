package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import dagger.Module
import dagger.Provides

@Module
class ClassesItemFragmentModule {

    @Provides
    fun provideListAdapter() = ClassesAdapter()
}
