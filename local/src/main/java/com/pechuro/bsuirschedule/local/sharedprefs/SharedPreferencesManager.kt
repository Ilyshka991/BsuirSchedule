package com.pechuro.bsuirschedule.local.sharedprefs

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(
        private val preferences: SharedPreferences,
        moshi: Moshi
) {

    companion object {
        private const val KEY_OPENED_SCHEDULE = "KEY_OPENED_SCHEDULE"
    }

    private val openedScheduleJsonAdapter = moshi.adapter(LastOpenedSchedule::class.java)

    fun getLastOpenedSchedule(): Flow<LastOpenedSchedule?> = callbackFlow {
        val sendNewValueCallback = {
            val defaultValue = openedScheduleJsonAdapter.toJson(null)
            val jsonString = get(KEY_OPENED_SCHEDULE, defaultValue)
            if (!isClosedForSend) offer(openedScheduleJsonAdapter.fromJson(jsonString))
        }
        sendNewValueCallback()
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_OPENED_SCHEDULE) sendNewValueCallback()
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.flowOn(Dispatchers.IO)

    fun setLastOpenedSchedule(schedule: LastOpenedSchedule?) {
        val jsonString = openedScheduleJsonAdapter.toJson(schedule)
        put(KEY_OPENED_SCHEDULE, jsonString)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(key: String, defaultValue: T) = with(preferences) {
        val result: Any = when (defaultValue) {
            is Boolean -> getBoolean(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is String -> getString(key, defaultValue) ?: defaultValue
            else -> throw IllegalArgumentException("Not supported type")
        }
        result as T
    }

    private fun <T> put(key: String, value: T) = with(preferences.edit()) {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is String -> putString(key, value)
            else -> throw IllegalArgumentException("Not supported type")
        }.apply()
    }
}