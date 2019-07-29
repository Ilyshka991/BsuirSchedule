package com.pechuro.bsuirschedule.di.module

import android.content.Context
import android.content.SharedPreferences
import com.pechuro.bsuirschedule.data.prefs.PrefsEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefsModule {

    @Provides
    @Singleton
    fun provideDefaultPrefs(context: Context): SharedPreferences {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.registerOnSharedPreferenceChangeListener { _, key -> EventBus.publish(PrefsEvent.OnChanged(key)) }
        return prefs
    }
}