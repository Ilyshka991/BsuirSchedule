package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastUpdateDTO(
        @Json(name = "lastUpdateDate")
        val lastUpdateDate: String
)
