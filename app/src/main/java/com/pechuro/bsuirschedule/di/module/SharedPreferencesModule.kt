package com.pechuro.bsuirschedule.di.module

import android.content.Context
import android.content.SharedPreferences
import com.pechuro.bsuirschedule.common.EventBus
import com.pechuro.bsuirschedule.common.SharedPreferencesEvent
import com.pechuro.bsuirschedule.di.annotations.AppScope
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @Provides
    @AppScope
    fun provideDefaultPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).apply {
                registerOnSharedPreferenceChangeListener { _, key ->
                    EventBus.publish(SharedPreferencesEvent.OnChanged(key))
                }
            }
}