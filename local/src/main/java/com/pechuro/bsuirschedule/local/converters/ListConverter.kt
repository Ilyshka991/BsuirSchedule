package com.pechuro.bsuirschedule.local.converters

import androidx.room.TypeConverter

class ListConverter {

    companion object {

        private const val LIST_SEPARATOR = ","
    }

    @TypeConverter
    fun fromIntList(value: List<Int>) = value.joinToString(separator = LIST_SEPARATOR)

    @TypeConverter
    fun stringToIntList(value: String) = value.split(LIST_SEPARATOR).mapNotNull { it.toIntOrNull() }
}