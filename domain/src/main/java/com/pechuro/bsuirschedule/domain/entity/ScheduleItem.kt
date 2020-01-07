package com.pechuro.bsuirschedule.domain.entity

data class ScheduleItem(
        val id: Long,
        val scheduleId: Long,
        val subject: String,
        val weekNumbers: List<Int>,
        val subgroupNumber: Int?,
        val lessonType: String,
        val auditories: List<Auditory>?,
        val note: String?,
        val startTime: String,
        val endTime: String,
        val weekDay: WeekDay,
        val employees: List<Employee>?
)