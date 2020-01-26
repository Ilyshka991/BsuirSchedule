package com.pechuro.bsuirschedule.data.utils

import com.pechuro.bsuirschedule.domain.entity.WeekDay

fun getWeekdayFor(value: String) = when (value) {
    "Понедельник" -> WeekDay.MONDAY
    "Вторник" -> WeekDay.TUESDAY
    "Среда" -> WeekDay.WEDNESDAY
    "Четверг" -> WeekDay.THURSDAY
    "Пятница" -> WeekDay.FRIDAY
    "Суббота" -> WeekDay.SATURDAY
    "Воскресение" -> WeekDay.SUNDAY
    else -> WeekDay.SUNDAY
}