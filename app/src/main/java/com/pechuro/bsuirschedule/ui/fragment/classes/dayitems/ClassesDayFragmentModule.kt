package com.pechuro.bsuirschedule.ui.fragment.classes.dayitems

import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.ui.fragment.classes.dayitems.adapter.ClassesDayAdapter
import dagger.Module
import dagger.Provides

@Module
class ClassesDayFragmentModule {

    @Provides
    fun provideLinearLayoutManager(fragment: ClassesDayFragment) =
            LinearLayoutManager(fragment.context)

    @Provides
    fun provideListAdapter() = ClassesDayAdapter()
}
