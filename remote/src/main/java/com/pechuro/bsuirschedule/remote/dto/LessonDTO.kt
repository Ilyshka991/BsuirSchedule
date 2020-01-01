package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LessonDTO(
        @Json(name = "id")
        val id: Long,
        @Json(name = "scheduleId")
        val scheduleId: Long,
        @Json(name = "subject")
        val subject: String?,
        @Json(name = "weekNumber")
        val weekNumber: String?,
        @Json(name = "subgroupNumber")
        val subgroupNumber: Int?,
        @Json(name = "lessonType")
        val lessonType: String?,
        @Json(name = "auditories")
        val auditories: List<String>?,
        @Json(name = "note")
        val note: String?,
        @Json(name = "startTime")
        val startTime: String?,
        @Json(name = "endTime")
        val endTime: String?,
        @Json(name = "weekDay")
        val weekDay: String,
        @Json(name = "employees")
        val employees: List<EmployeeDTO>?
)