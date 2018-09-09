package com.pechuro.bsuirschedule.repository.api

import com.google.gson.annotations.SerializedName
import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem

data class Response(val employee: Employee?,
                    val studentGroup: Group?,
                    val schedules: List<ScheduleResponse>?,
                    @SerializedName("examSchedules")
                    val exams: List<ScheduleResponse>?)

data class ScheduleResponse(
        val weekDay: String,
        @SerializedName("schedule")
        val classes: List<ScheduleItem>
)
