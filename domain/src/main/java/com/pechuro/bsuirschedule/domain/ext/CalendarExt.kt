package com.pechuro.bsuirschedule.domain.ext

import com.pechuro.bsuirschedule.domain.entity.WeekDay
import java.util.Calendar

fun Calendar.addDays(days: Int) = apply {
    add(Calendar.DATE, days)
}

fun Calendar.getWeekDay(): WeekDay {
    var weekDayIndex = get(Calendar.DAY_OF_WEEK) - 2
    if (weekDayIndex == -1) weekDayIndex = 6
    return WeekDay.getForIndex(weekDayIndex)
}