package com.pechuro.bsuirschedule.domain.entity

sealed class ScheduleItem(
        val id: Long,
        val subject: String?,
        val weekNumbers: List<Int>,
        val subgroupNumber: Int,
        val lessonType: String?,
        val note: String?,
        val startTime: String?,
        val endTime: String?,
        val weekDay: String?,
        val auditories: List<Auditory>?
) {

    class EmployeeScheduleItem(
            id: Long,
            subject: String?,
            weekNumbers: List<Int>,
            subgroupNumber: Int,
            lessonType: String?,
            note: String?,
            startTime: String?,
            endTime: String?,
            weekDay: String?,
            auditories: List<Auditory>?,
            val schedule: Schedule.EmployeeSchedule,
            val studentGroups: List<Group>?
    ) : ScheduleItem(
            id = id,
            subject = subject,
            weekNumbers = weekNumbers,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay,
            auditories = auditories
    )

    class GroupScheduleItem(
            id: Long,
            subject: String?,
            weekNumbers: List<Int>,
            subgroupNumber: Int,
            lessonType: String?,
            note: String?,
            startTime: String?,
            endTime: String?,
            weekDay: String?,
            auditories: List<Auditory>?,
            val schedule: Schedule.GroupSchedule,
            val employees: List<Employee>?
    ) : ScheduleItem(
            id = id,
            subject = subject,
            weekNumbers = weekNumbers,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            weekDay = weekDay,
            auditories = auditories
    )
}