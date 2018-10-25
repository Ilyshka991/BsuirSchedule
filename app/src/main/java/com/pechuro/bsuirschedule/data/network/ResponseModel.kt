package com.pechuro.bsuirschedule.data.network

import com.google.gson.annotations.SerializedName
import com.pechuro.bsuirschedule.data.entity.Employee
import com.pechuro.bsuirschedule.data.entity.Group
import com.pechuro.bsuirschedule.data.entity.ScheduleItem

class ResponseModel(val employee: Employee?,
                    val studentGroup: Group?,
                    @SerializedName("schedules")
                    val schedule: List<ScheduleResponse>?,
                    @SerializedName("examSchedules")
                    val exam: List<ScheduleResponse>?)

class ScheduleResponse(
        val weekDay: String,
        @SerializedName("schedule")
        val classes: List<ScheduleItem>
)


class LastUpdateResponse(val lastUpdateDate: String)