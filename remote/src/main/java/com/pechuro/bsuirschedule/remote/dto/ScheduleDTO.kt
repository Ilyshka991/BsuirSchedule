package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleDTO(
    @Json(name = "schedules")
    val schedule: Map<String, List<LessonDTO>>?,
    @Json(name = "exams")
    val exam: List<LessonDTO>?
)
