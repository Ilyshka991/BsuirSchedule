package com.pechuro.bsuirschedule.ui.fragment.exam

import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamAdapter
import dagger.Module
import dagger.Provides

@Module
class ExamFragmentModule {

    @Provides
    fun provideListAdapter() = ExamAdapter()
}
