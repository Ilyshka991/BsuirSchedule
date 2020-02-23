package com.pechuro.bsuirschedule.local.sharedprefs

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(
        private val preferences: SharedPreferences,
        moshi: Moshi
) {

    companion object {
        private const val KEY_OPENED_SCHEDULE = "KEY_OPENED_SCHEDULE"
    }

    private val openedScheduleJsonAdapter = moshi.adapter(LastOpenedSchedule::class.java)

    var lastOpenedSchedule: LastOpenedSchedule?
        set(value) {
            val jsonString = openedScheduleJsonAdapter.toJson(value)
            put(KEY_OPENED_SCHEDULE, jsonString)
        }
        get() {
            val jsonString = get(KEY_OPENED_SCHEDULE, "")
            return openedScheduleJsonAdapter.fromJson(jsonString)
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