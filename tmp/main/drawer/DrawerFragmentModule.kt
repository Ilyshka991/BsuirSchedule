package com.pechuro.bsuirschedule.feature.main.drawer

import android.content.Context
import com.pechuro.bsuirschedule.di.annotations.FragmentScope
import com.pechuro.bsuirschedule.feature.main.drawer.adapter.NavItemAdapter
import com.pechuro.bsuirschedule.feature.main.drawer.adapter.NavItemsDiffCallback
import dagger.Module
import dagger.Provides

@Module
class DrawerFragmentModule {

    @Provides
    @FragmentScope
    fun provideNavItemsDiffCallback() = NavItemsDiffCallback()

    @Provides
    @FragmentScope
    fun provideAdapter(context: Context, diffCallback: NavItemsDiffCallback) =
            NavItemAdapter(context, diffCallback)

}