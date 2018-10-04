package com.pechuro.bsuirschedule.data.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeekNumberTypeAdapter : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
        val array = json?.asJsonArray
        var result = ""
        array?.forEach { result += it.asString + " " }
        return result
    }
}