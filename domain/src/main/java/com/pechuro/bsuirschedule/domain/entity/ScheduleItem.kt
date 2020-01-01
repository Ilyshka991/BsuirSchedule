package com.pechuro.bsuirschedule.domain.entity

data class ScheduleItem(
        val id: Long,
        val scheduleId: Long,
        val subject: String?,
        val weekNumber: String?,
        val subgroupNumber: Int?,
        val lessonType: String?,
        val auditories: List<String>?,
        val note: String?,
        val startTime: String?,
        val endTime: String?,
        val weekDay: String,
        val employees: List<Employee>?
)