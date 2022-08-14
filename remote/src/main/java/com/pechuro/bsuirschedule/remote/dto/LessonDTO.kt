package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LessonDTO(
    @Json(name = "subject")
    val subject: String?,
    @Json(name = "weekNumber")
    val weekNumber: List<Int>,
    @Json(name = "numSubgroup")
    val subgroupNumber: Int,
    @Json(name = "lessonType")
    val lessonType: String?,
    @Json(name = "auditory")
    val auditories: List<String>?,
    @Json(name = "note")
    val note: String?,
    @Json(name = "startLessonTime")
    val startTime: String?,
    @Json(name = "endLessonTime")
    val endTime: String?,
    @Json(name = "employee")
    val employees: List<EmployeeDTO>?,
    @Json(name = "studentGroup")
    val studentGroups: List<String>?
)