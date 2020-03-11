package com.pechuro.bsuirschedule.feature.displayschedule.data

import com.pechuro.bsuirschedule.domain.entity.WeekNumber

sealed class DisplayScheduleViewType {

    data class DayClasses(
            val startWeekNumber: WeekNumber
    ) : DisplayScheduleViewType()

    object WeekClasses : DisplayScheduleViewType()

    object Exams : DisplayScheduleViewType()
}