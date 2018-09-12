package com.pechuro.bsuirschedule.repository.api

import com.google.gson.annotations.SerializedName
import com.pechuro.bsuirschedule.repository.entity.Employee
import com.pechuro.bsuirschedule.repository.entity.Group
import com.pechuro.bsuirschedule.repository.entity.ScheduleItem

open class Response(val employee: Employee?,
                    val studentGroup: Group?,
                    @SerializedName("schedules")
                    val schedule: List<ScheduleResponse>?,
                    @SerializedName("examSchedules")
                    val exam: List<ScheduleResponse>?)

class ResponseError(val error: Throwable) : Response(null, null, null, null)

class ScheduleResponse(
        val weekDay: String,
        @SerializedName("schedule")
        val classes: List<ScheduleItem>
)

class LastUpdateResponse(val lastUpdateDate: String?)
