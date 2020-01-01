package com.pechuro.bsuirschedule.feature.main.exam

import com.pechuro.bsuirschedule.feature.main.exam.adapter.ExamAdapter
import com.pechuro.bsuirschedule.feature.main.exam.adapter.ExamsDiffCallback
import dagger.Module
import dagger.Provides

@Module
class ExamFragmentModule {

    @Provides
    fun provideClassesDiffCallback() = ExamsDiffCallback()

    @Provides
    fun provideListAdapter(diffCallback: ExamsDiffCallback) = ExamAdapter(diffCallback)
}
