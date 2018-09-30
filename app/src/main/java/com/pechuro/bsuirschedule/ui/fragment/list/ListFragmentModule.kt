package com.pechuro.bsuirschedule.ui.fragment.list

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides

@Module
class ListFragmentModule {

    @Provides
    fun provideLinearLayoutManager(fragment: ListFragment) =
            LinearLayoutManager(fragment.context)

    @Provides
    fun provideListAdapter() = ListAdapter()
}
