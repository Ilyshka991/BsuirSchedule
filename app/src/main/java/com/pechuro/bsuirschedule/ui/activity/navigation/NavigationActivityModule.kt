package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemAdapter
import dagger.Module
import dagger.Provides

@Module
class NavigationActivityModule {
    @Provides
    fun provideAdapter() = NavItemAdapter()

    @Provides
    fun provideMa(activity: NavigationActivity) =
            LinearLayoutManager(activity)
}

