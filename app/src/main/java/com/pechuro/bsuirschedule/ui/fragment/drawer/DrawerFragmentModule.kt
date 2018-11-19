package com.pechuro.bsuirschedule.ui.fragment.drawer

import android.content.Context
import com.pechuro.bsuirschedule.ui.fragment.drawer.adapter.NavItemAdapter
import com.pechuro.bsuirschedule.ui.fragment.drawer.adapter.NavItemsDiffCallback
import dagger.Module
import dagger.Provides

@Module
class DrawerFragmentModule {
    @Provides
    fun provideAdapter(context: Context, diffCallback: NavItemsDiffCallback) =
            NavItemAdapter(context, diffCallback)

}