package com.pechuro.bsuirschedule.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleDTO(
        @Json(name = "employee")
        val employee: EmployeeDTO?,
        @Json(name = "studentGroup")
        val studentGroup: GroupDTO?,
        @Json(name = "schedules")
        val schedule: List<ScheduleItemDTO>?,
        @Json(name = "examSchedules")
        val exam: List<ScheduleItemDTO>?
)
