package com.pechuro.bsuirschedule.local.sharedprefs

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(
        private val preferences: SharedPreferences,
        moshi: Moshi
) {

    companion object {
        private const val KEY_OPENED_SCHEDULE = "KEY_OPENED_SCHEDULE"
        private const val KEY_DISPLAY_TYPE = "KEY_DISPLAY_TYPE"
        private const val KEY_SUBGROUP_NUMBER = "KEY_SUBGROUP_NUMBER"
        private const val KEY_APP_THEME = "KEY_APP_THEME"
        private const val KEY_NAVIGATION_HINT_SHOWN = "KEY_NAVIGATION_HINT_SHOWN"
        private const val KEY_SCHEDULE_HINT_SHOWN = "KEY_SCHEDULE_HINT_SHOWN"
        private const val KEY_SCHEDULE_WIDGET_INFO = "KEY_SCHEDULE_WIDGET_INFO"
    }

    private val openedScheduleJsonAdapter = moshi.adapter(LocalScheduleInfo::class.java)
    private val widgetInfoJsonAdapter = moshi.adapter(LocalScheduleWidgetInfo::class.java)

    fun getLastOpenedSchedule(): Flow<LocalScheduleInfo?> = getFlow(KEY_OPENED_SCHEDULE) {
        openedScheduleJsonAdapter.toJson(null)
    }
            .map { openedScheduleJsonAdapter.fromJson(it) }
            .flowOn(Dispatchers.IO)

    fun setLastOpenedSchedule(schedule: LocalScheduleInfo?) {
        val jsonString = openedScheduleJsonAdapter.toJson(schedule)
        put(KEY_OPENED_SCHEDULE, jsonString)
    }

    fun getDisplayType(default: Int): Flow<Int> = getFlow(KEY_DISPLAY_TYPE) { default }

    fun setDisplayType(type: Int) {
        put(KEY_DISPLAY_TYPE, type)
    }

    fun getSubgroupNumber(default: Int): Flow<Int> = getFlow(KEY_SUBGROUP_NUMBER) { default }

    fun setSubgroupNumber(type: Int) {
        put(KEY_SUBGROUP_NUMBER, type)
    }

    fun getAppTheme(default: String) = get(KEY_APP_THEME, default)

    fun setAppTheme(value: String) = put(KEY_APP_THEME, value)

    fun getNavigationHintDisplayState(default: Boolean): Flow<Boolean> = getFlow(KEY_NAVIGATION_HINT_SHOWN) { default }

    fun setNavigationHintDisplayState(shown: Boolean) {
        put(KEY_NAVIGATION_HINT_SHOWN, shown)
    }

    fun getScheduleHintDisplayState(default: Boolean): Flow<Boolean> = getFlow(KEY_SCHEDULE_HINT_SHOWN) { default }

    fun setScheduleHintDisplayState(shown: Boolean) {
        put(KEY_SCHEDULE_HINT_SHOWN, shown)
    }

    fun getAllScheduleWidgetInfoList(): List<LocalScheduleWidgetInfo> = preferences.all
            .filterKeys {
                it.startsWith(KEY_SCHEDULE_WIDGET_INFO)
            }
            .keys
            .mapNotNull { key ->
                val jsonValue = get(key, widgetInfoJsonAdapter.toJson(null))
                widgetInfoJsonAdapter.fromJson(jsonValue)
            }

    fun getScheduleWidgetInfo(widgetId: Int): LocalScheduleWidgetInfo? {
        val key = buildScheduleWidgetInfoPrefsKey(widgetId)
        val jsonValue = get(key, widgetInfoJsonAdapter.toJson(null))
        return widgetInfoJsonAdapter.fromJson(jsonValue)
    }

    fun addScheduleWidgetInfo(info: LocalScheduleWidgetInfo) {
        val key = buildScheduleWidgetInfoPrefsKey(info.widgetId)
        val jsonValue = widgetInfoJsonAdapter.toJson(info)
        put(key, jsonValue)
    }

    fun removeScheduleWidgetInfo(id: Int) {
        val key = buildScheduleWidgetInfoPrefsKey(id)
        preferences.edit().remove(key).apply()
    }

    private fun buildScheduleWidgetInfoPrefsKey(widgetId: Int) = "${KEY_SCHEDULE_WIDGET_INFO}_$widgetId"

    private fun <T : Any> getFlow(key: String, defaultValue: () -> T): Flow<T> = callbackFlow {
        val sendNewValueCallback = {
            val value = get(key, defaultValue())
            if (!isClosedForSend) offer(value)
        }
        sendNewValueCallback()
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) sendNewValueCallback()
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.flowOn(Dispatchers.IO)

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