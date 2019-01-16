package com.pechuro.bsuirschedule.ui.activity.navigation

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides

@Module
class NavigationActivityModule {

    @Provides
    fun provideLinearLayoutManager(activity: NavigationActivity) = LinearLayoutManager(activity)
}
