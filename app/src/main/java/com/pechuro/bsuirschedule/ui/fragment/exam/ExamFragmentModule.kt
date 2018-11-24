package com.pechuro.bsuirschedule.ui.fragment.exam

import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamAdapter
import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamsDiffCallback
import dagger.Module
import dagger.Provides

@Module
class ExamFragmentModule {

    @Provides
    fun provideClassesDiffCallback() = ExamsDiffCallback()

    @Provides
    fun provideListAdapter(diffCallback: ExamsDiffCallback) = ExamAdapter(diffCallback)
}
