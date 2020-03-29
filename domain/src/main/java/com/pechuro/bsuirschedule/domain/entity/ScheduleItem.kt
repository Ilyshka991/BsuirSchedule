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
            override val employees: List<Employee>
    ) : ScheduleItem(), ILesson, IGroupEvent

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
            override val employees: List<Employee>
    ) : ScheduleItem(), IExam, IGroupEvent

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
            override val studentGroups: List<Group>
    ) : ScheduleItem(), ILesson, IEmployeeEvent

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
            override val studentGroups: List<Group>
    ) : ScheduleItem(), IExam, IEmployeeEvent
}
