package com.pechuro.bsuirschedule.ui.fragment.classes

import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class ClassesFragmentModule {

    @Provides
    fun providePagerAdapter(fragment: ClassesFragment) =
            ClassesPagerAdapter(fragment.childFragmentManager)

    @Provides
    @FragmentScope
    fun provideRvPool() = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(1, 20)
    }
}