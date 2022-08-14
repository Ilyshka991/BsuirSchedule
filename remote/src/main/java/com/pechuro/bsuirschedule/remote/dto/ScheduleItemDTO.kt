package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleItemDTO(
    @Json(name = "weekDay")
    val weekDay: String,
    @Json(name = "schedule")
    val classes: List<LessonDTO>
)