package com.pechuro.bsuirschedule.data.prefs

import com.pechuro.bsuirschedule.App
import com.pechuro.bsuirschedule.ui.data.ScheduleInformation
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefsDelegate<T>(private val key: String, private val defaultValue: T) :
        ReadWriteProperty<Any?, T> {

    private val prefs = App.appComponent.getSharedPrefs()
    private val gson = App.appComponent.getGson()

    override fun getValue(thisRef: Any?, property: KProperty<*>) = get(key, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = save(key, value)

    @Suppress("UNCHECKED_CAST")
    fun get(key: String, defaultValue: T) = with(prefs) {
        val result: Any = when (defaultValue) {
            is Boolean -> getBoolean(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is String -> getString(key, defaultValue)

            is ScheduleInformation -> {
                println(prefs)
                println(this@PrefsDelegate)
                gson.fromJson(getString(key, SCHEDULE_INFORMATION_DEFAULT_VALUE),
                        ScheduleInformation::class.java)
            }

            else -> throw IllegalArgumentException()
        }
        result as T
    }

    private fun save(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is String -> putString(key, value)
            is ScheduleInformation -> putString(key, gson.toJson(value))
            else -> throw IllegalArgumentException()
        }.apply()
    }

    companion object {
        private const val SCHEDULE_INFORMATION_DEFAULT_VALUE = "{\"id\":-1,\"name\":\"\",\"type\":-1}"
    }
}