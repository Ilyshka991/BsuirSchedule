package com.pechuro.bsuirschedule.repository.utils

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pechuro.bsuirschedule.repository.entity.Employee


class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList(value: List<Int>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromString1(value: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList1(value: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }


    @TypeConverter
    fun fromString2(value: String): List<Employee> {
        val gson = Gson()
        val type = object : TypeToken<List<Employee>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList2(value: List<Employee>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Employee>>() {}.type
        return gson.toJson(value, type)
    }
}