package com.pechuro.bsuirschedule.feature.display.data

import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import com.pechuro.bsuirschedule.domain.entity.WeekNumber

sealed class DisplayScheduleItem(
        open val scheduleItem: ScheduleItem?
) {

    data class DayClasses(
            override val scheduleItem: Lesson
    ) : DisplayScheduleItem(scheduleItem)

    data class WeekClasses(
            override val scheduleItem: Lesson,
            val itemsIdList: List<Long>,
            val weekNumbers: List<WeekNumber>
    ) : DisplayScheduleItem(scheduleItem)

    data class Exams(
            override val scheduleItem: Exam
    ) : DisplayScheduleItem(scheduleItem)

    object Empty : DisplayScheduleItem(null)
}