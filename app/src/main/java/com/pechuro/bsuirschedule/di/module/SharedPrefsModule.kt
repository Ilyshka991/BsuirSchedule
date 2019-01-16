package com.pechuro.bsuirschedule.di.module

import android.content.Context
import android.content.SharedPreferences
import com.pechuro.bsuirschedule.data.prefs.PrefsEvent
import com.pechuro.bsuirschedule.ui.utils.EventBus
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Singleton

@Module
class SharedPrefsModule {

    @Provides
    @Singleton
    fun provideDefaultPrefs(context: Context): SharedPreferences {
        val prefs = context.defaultSharedPreferences
        prefs.registerOnSharedPreferenceChangeListener { _, key -> EventBus.publish(PrefsEvent.OnChanged(key)) }
        return prefs
    }
}