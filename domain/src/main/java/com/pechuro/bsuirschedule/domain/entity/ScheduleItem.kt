package com.pechuro.bsuirschedule.domain.entity

import kotlinx.android.parcel.Parcelize
import java.util.*

sealed class ScheduleItem : IScheduleItem {

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
    ) : ScheduleItem(), ILesson

    @Parcelize
    data class GroupExam(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
            override val date: Date,
            val employees: List<Employee>
    ) : ScheduleItem(), IExam

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
    ) : ScheduleItem(), ILesson

    @Parcelize
    data class EmployeeExam(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
            override val date: Date,
            val studentGroups: List<Group>
    ) : ScheduleItem(), IExam
}
