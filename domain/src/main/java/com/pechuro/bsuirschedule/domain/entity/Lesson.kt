package com.pechuro.bsuirschedule.domain.entity

import kotlinx.android.parcel.Parcelize

sealed class Lesson(
        open val weekDay: WeekDay,
        open val weekNumber: WeekNumber,
        open val priority: LessonPriority
) : ScheduleItem {

    fun copy(
            id: Long = this.id,
            subject: String = this.subject,
            subgroupNumber: SubgroupNumber = this.subgroupNumber,
            lessonType: String = this.lessonType,
            note: String = this.note,
            startTime: LocalTime = this.startTime,
            endTime: LocalTime = this.endTime,
            auditories: List<Auditory> = this.auditories,
            isAddedByUser: Boolean = this.isAddedByUser,
            priority: LessonPriority = this.priority,
            weekDay: WeekDay = this.weekDay,
            weekNumber: WeekNumber = this.weekNumber
    ) = when (this) {
        is GroupLesson -> GroupLesson(id = id,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                note = note,
                startTime = startTime,
                endTime = endTime,
                auditories = auditories,
                isAddedByUser = isAddedByUser,
                priority = priority,
                weekDay = weekDay,
                weekNumber = weekNumber,
                employees = this.employees
        )
        is EmployeeLesson -> EmployeeLesson(id = id,
                subject = subject,
                subgroupNumber = subgroupNumber,
                lessonType = lessonType,
                note = note,
                startTime = startTime,
                endTime = endTime,
                auditories = auditories,
                isAddedByUser = isAddedByUser,
                priority = priority,
                weekDay = weekDay,
                weekNumber = weekNumber,
                studentGroups = this.studentGroups
        )
    }

    @Parcelize
    data class GroupLesson(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: LocalTime,
            override val endTime: LocalTime,
            override val auditories: List<Auditory>,
            override val isAddedByUser: Boolean,
            override val priority: LessonPriority,
            override val weekDay: WeekDay,
            override val weekNumber: WeekNumber,
            val employees: List<Employee>
    ) : Lesson(
            weekDay = weekDay,
            weekNumber = weekNumber,
            priority = priority
    )

    @Parcelize
    data class EmployeeLesson(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: LocalTime,
            override val endTime: LocalTime,
            override val auditories: List<Auditory>,
            override val isAddedByUser: Boolean,
            override val priority: LessonPriority,
            override val weekDay: WeekDay,
            override val weekNumber: WeekNumber,
            val studentGroups: List<Group>
    ) : Lesson(
            weekDay = weekDay,
            weekNumber = weekNumber,
            priority = priority
    )
}