package com.pechuro.bsuirschedule.domain.entity

sealed class Schedule(
        val name: String,
        val type: ScheduleType
) {

    class GroupSchedule(
            name: String,
            type: ScheduleType,
            val lastUpdated: String?,
            val group: Group
    ) : Schedule(
            name = name,
            type = type
    )

    class EmployeeSchedule(
            name: String,
            type: ScheduleType,
            val employee: Employee
    ) : Schedule(
            name = name,
            type = type
    )
}