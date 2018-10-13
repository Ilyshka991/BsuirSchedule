package com.pechuro.bsuirschedule.ui.fragment.exam

import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.ui.fragment.exam.adapter.ExamAdapter
import dagger.Module
import dagger.Provides

@Module
class ExamFragmentModule {

    @Provides
    fun provideLinearLayoutManager(fragment: ExamFragment) =
            LinearLayoutManager(fragment.context)

    @Provides
    fun provideListAdapter() = ExamAdapter()
}
