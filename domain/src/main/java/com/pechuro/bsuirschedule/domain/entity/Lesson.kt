package com.pechuro.bsuirschedule.domain.entity

import kotlinx.android.parcel.Parcelize

sealed class Lesson(
        open val weekDay: WeekDay,
        open val weekNumber: WeekNumber,
        open val priority: LessonPriority
) : ScheduleItem {

    @Parcelize
    data class GroupLesson(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
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
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
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