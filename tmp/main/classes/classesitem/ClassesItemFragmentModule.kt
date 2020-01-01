package com.pechuro.bsuirschedule.feature.main.classes.classesitem

import com.pechuro.bsuirschedule.di.annotations.ChildFragmentScope
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesAdapter
import com.pechuro.bsuirschedule.ui.fragment.classes.classesitem.adapter.ClassesDiffCallback
import dagger.Module
import dagger.Provides

@Module
class ClassesItemFragmentModule {

    @Provides
    @ChildFragmentScope
    fun provideClassesDiffCallback() = ClassesDiffCallback()

    @Provides
    @ChildFragmentScope
    fun provideListAdapter(diffCallback: ClassesDiffCallback) = ClassesAdapter(diffCallback)
}
