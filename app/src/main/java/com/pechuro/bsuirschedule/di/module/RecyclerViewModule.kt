package com.pechuro.bsuirschedule.di.module

import androidx.recyclerview.widget.RecyclerView
import com.pechuro.bsuirschedule.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class RecyclerViewModule {

    @Provides
    @AppScope
    fun provideRecycledViewPool() = RecyclerView.RecycledViewPool()
}