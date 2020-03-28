package com.pechuro.bsuirschedule.feature.displayschedule.data

import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekNumber

sealed class DisplayScheduleItem(
        open val scheduleItem: ScheduleItem?
) {

    data class GroupDayClasses(
            override val scheduleItem: ScheduleItem.GroupLesson
    ) : DisplayScheduleItem(scheduleItem)

    data class GroupWeekClasses(
            override val scheduleItem: ScheduleItem.GroupLesson,
            val itemsIdList: List<Long>,
            val weekNumbers: List<WeekNumber>
    ) : DisplayScheduleItem(scheduleItem)

    data class GroupExams(
            override val scheduleItem: ScheduleItem.GroupExam
    ) : DisplayScheduleItem(scheduleItem)

    data class EmployeeDayClasses(
            override val scheduleItem: ScheduleItem.EmployeeLesson
    ) : DisplayScheduleItem(scheduleItem)

    data class EmployeeWeekClasses(
            override val scheduleItem: ScheduleItem.EmployeeLesson,
            val itemsIdList: List<Long>,
            val weekNumbers: List<WeekNumber>
    ) : DisplayScheduleItem(scheduleItem)

    data class EmployeeExams(
            override val scheduleItem: ScheduleItem.EmployeeExam
    ) : DisplayScheduleItem(scheduleItem)

    object Empty : DisplayScheduleItem(null)
}