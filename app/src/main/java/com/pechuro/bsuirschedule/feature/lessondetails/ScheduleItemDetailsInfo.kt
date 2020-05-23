package com.pechuro.bsuirschedule.feature.lessondetails

import com.pechuro.bsuirschedule.domain.entity.*

sealed class ScheduleItemDetailsInfo {

    companion object {
        const val VIEW_TYPE_TIME = 1
        const val VIEW_TYPE_EMPLOYEE_INFO = 2
        const val VIEW_TYPE_WEEKS = 3
        const val VIEW_TYPE_AUDITORY_HEADER = 4
        const val VIEW_TYPE_AUDITORY_INFO = 5
        const val VIEW_TYPE_NOTE = 6
        const val VIEW_TYPE_GROUP_INFO = 7
        const val VIEW_TYPE_PRIORITY = 8
        const val VIEW_TYPE_SUBGROUP_NUMBER = 9
        const val VIEW_TYPE_DATE = 10
        const val VIEW_TYPE_LESSON_TYPE = 11
    }

    val viewType: Int
        get() = when (this) {
            is Time -> VIEW_TYPE_TIME
            is EmployeeInfo -> VIEW_TYPE_EMPLOYEE_INFO
            is Weeks -> VIEW_TYPE_WEEKS
            is AuditoryHeader -> VIEW_TYPE_AUDITORY_HEADER
            is AuditoryInfo -> VIEW_TYPE_AUDITORY_INFO
            is Note -> VIEW_TYPE_NOTE
            is GroupInfo -> VIEW_TYPE_GROUP_INFO
            is Priority -> VIEW_TYPE_PRIORITY
            is Subgroup -> VIEW_TYPE_SUBGROUP_NUMBER
            is Date -> VIEW_TYPE_DATE
            is LessonType -> VIEW_TYPE_LESSON_TYPE
        }

    data class Time(
            val startTime: LocalTime,
            val endTime: LocalTime
    ) : ScheduleItemDetailsInfo()

    data class EmployeeInfo(val employees: List<Employee>) : ScheduleItemDetailsInfo()

    data class GroupInfo(val groups: List<Group>) : ScheduleItemDetailsInfo()

    data class Weeks(val weeks: List<WeekNumber>) : ScheduleItemDetailsInfo()

    object AuditoryHeader : ScheduleItemDetailsInfo()

    data class AuditoryInfo(val auditory: Auditory) : ScheduleItemDetailsInfo()

    data class Note(
            val note: String
    ) : ScheduleItemDetailsInfo()

    data class Priority(
            val priority: LessonPriority
    ) : ScheduleItemDetailsInfo()

    data class Subgroup(
            val subgroupNumber: SubgroupNumber
    ) : ScheduleItemDetailsInfo()

    data class Date(
            val date: LocalDate
    ) : ScheduleItemDetailsInfo()

    data class LessonType(
            val type: String
    ) : ScheduleItemDetailsInfo()
}
