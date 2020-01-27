package com.pechuro.bsuirschedule.domain.entity

data class ScheduleItem(
        val id: Long,
        val schedule: Schedule,
        val subject: String?,
        val weekNumbers: List<Int>,
        val subgroupNumber: Int,
        val lessonType: String?,
        val note: String?,
        val startTime: String?,
        val endTime: String?,
        val weekDay: String?,
        val employees: List<Employee>?,
        val auditories: List<Auditory>?,
        val studentGroups: List<Group>?
)