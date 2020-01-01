package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnnouncementDTO(
        @Json(name = "date")
        val date: String,
        @Json(name = "employee")
        val employeeName: String?,
        @Json(name = "content")
        val content: String
)
