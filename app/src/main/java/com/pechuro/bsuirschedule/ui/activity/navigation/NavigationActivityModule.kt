package com.pechuro.bsuirschedule.ui.activity.navigation

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.pechuro.bsuirschedule.ui.activity.navigation.adapter.NavItemAdapter
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.defaultSharedPreferences

@Module
class NavigationActivityModule {
    @Provides
    fun provideAdapter(context: Context) = NavItemAdapter(context)

    @Provides
    fun provideLinearLayoutManager(activity: NavigationActivity) =
            LinearLayoutManager(activity)

    @Provides
    fun provideDefaultSharedPref(context: Context) = context.defaultSharedPreferences

    @Provides
    fun provideRxPref(pref: SharedPreferences) = RxSharedPreferences.create(pref)
}

