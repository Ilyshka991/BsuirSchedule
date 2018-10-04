package com.pechuro.bsuirschedule.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pechuro.bsuirschedule.data.entity.Employee


class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromEmployeeList(value: List<Employee>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Employee>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toEmployeeList(value: String): List<Employee> {
        val gson = Gson()
        val type = object : TypeToken<List<Employee>>() {}.type
        return gson.fromJson(value, type)
    }
}