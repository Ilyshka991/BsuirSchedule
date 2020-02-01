package com.pechuro.bsuirschedule.domain.entity

import java.util.*

sealed class ScheduleItem<T : Schedule>(
        val id: Long,
        val schedule: T,
        val subject: String,
        val subgroupNumber: Int,
        val lessonType: String,
        val note: String,
        val startTime: String,
        val endTime: String,
        val auditories: List<Auditory>
) {

    class GroupLesson(
            id: Long,
            schedule: Schedule.GroupClasses,
            subject: String,
            subgroupNumber: Int,
            lessonType: String,
            note: String,
            startTime: String,
            endTime: String,
            auditories: List<Auditory>,
            val weekDay: WeekDay,
            val weekNumber: Int,
            val employees: List<Employee>
    ) : ScheduleItem<Schedule.GroupClasses>(
            id = id,
            schedule = schedule,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            auditories = auditories
    )

    class GroupExam(
            id: Long,
            schedule: Schedule.GroupExams,
            subject: String,
            subgroupNumber: Int,
            lessonType: String,
            note: String,
            startTime: String,
            endTime: String,
            auditories: List<Auditory>,
            val date: Date,
            val employees: List<Employee>
    ) : ScheduleItem<Schedule.GroupExams>(
            id = id,
            schedule = schedule,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            auditories = auditories
    )

    class EmployeeLesson(
            id: Long,
            schedule: Schedule.EmployeeClasses,
            subject: String,
            subgroupNumber: Int,
            lessonType: String,
            note: String,
            startTime: String,
            endTime: String,
            auditories: List<Auditory>,
            val weekDay: WeekDay,
            val weekNumber: Int,
            val studentGroups: List<Group>
    ) : ScheduleItem<Schedule.EmployeeClasses>(
            id = id,
            schedule = schedule,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            auditories = auditories
    )

    class EmployeeExam(
            id: Long,
            schedule: Schedule.EmployeeExams,
            subject: String,
            subgroupNumber: Int,
            lessonType: String,
            note: String,
            startTime: String,
            endTime: String,
            auditories: List<Auditory>,
            val date: Date,
            val studentGroups: List<Group>
    ) : ScheduleItem<Schedule.EmployeeExams>(
            id = id,
            schedule = schedule,
            subject = subject,
            subgroupNumber = subgroupNumber,
            lessonType = lessonType,
            note = note,
            startTime = startTime,
            endTime = endTime,
            auditories = auditories
    )
}