package com.pechuro.bsuirschedule.feature.itemdetails

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Employee
import com.pechuro.bsuirschedule.domain.entity.Group
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.LocalDate
import com.pechuro.bsuirschedule.domain.entity.LocalTime
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber

sealed class ScheduleItemDetailsInfo {

    data class Time(
            val startTime: LocalTime,
            val endTime: LocalTime
    ) : ScheduleItemDetailsInfo()

    data class EmployeeInfo(val employees: List<Employee>) : ScheduleItemDetailsInfo()

    data class GroupInfo(val groups: List<Group>) : ScheduleItemDetailsInfo()

    data class LessonDate(
            val weekDay: WeekDay,
            val weeks: List<WeekNumber>
    ) : ScheduleItemDetailsInfo()

    object AuditoryInfoHeader : ScheduleItemDetailsInfo()

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

    data class ExamDate(
            val date: LocalDate
    ) : ScheduleItemDetailsInfo()
}
