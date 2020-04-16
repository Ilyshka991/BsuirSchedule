package com.pechuro.bsuirschedule.domain.entity

import java.util.*

enum class WeekNumber(val index: Int) {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOURTH(3);

    companion object {
        const val TOTAL_COUNT = 4

        fun getForIndex(index: Int) = values()[index]

        fun calculateCurrentWeekNumber(): WeekNumber {
            val currentCalendar = Calendar.getInstance()
            var year = currentCalendar.get(Calendar.YEAR)
            if (currentCalendar.get(Calendar.MONTH) < 8) year--
            val firstDayCalendar = Calendar.getInstance()
            firstDayCalendar.set(year, Calendar.SEPTEMBER, 1, 0, 0, 0)
            val difference = (currentCalendar.timeInMillis - firstDayCalendar.timeInMillis) / 1000 / 60 / 60 / 24
            var day = firstDayCalendar.get(GregorianCalendar.DAY_OF_WEEK)
            day -= 2
            if (day == -1) {
                day = 6
            }
            val weekNumberIndex = ((difference + day).toInt() / 7) % 4
            return WeekNumber.getForIndex(weekNumberIndex)
        }
    }
}