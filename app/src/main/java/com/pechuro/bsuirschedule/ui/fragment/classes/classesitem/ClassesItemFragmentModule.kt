package com.pechuro.bsuirschedule.ui.fragment.classes.classesitem

import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesDiffCallback
import dagger.Module
import dagger.Provides

@Module
class ClassesItemFragmentModule {

    @Provides
    fun provideClassesDiffCallback() = ClassesDiffCallback()

    @Provides
    fun provideListAdapter(diffCallback: ClassesDiffCallback) = ClassesAdapter(diffCallback)
}
