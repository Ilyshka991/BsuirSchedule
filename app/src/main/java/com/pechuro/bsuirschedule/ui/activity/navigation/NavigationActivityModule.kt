package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemAdapter
import dagger.Module
import dagger.Provides

@Module
class NavigationActivityModule {
    @Provides
    fun provideAdapter(context: Context) = NavItemAdapter(context)

    @Provides
    fun provideLinearLayoutManager(activity: NavigationActivity) =
            LinearLayoutManager(activity)
}

