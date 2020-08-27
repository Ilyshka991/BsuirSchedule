package com.pechuro.bsuirschedule.domain.entity

import java.util.*
import java.util.Calendar.*

enum class WeekNumber(val index: Int) {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOURTH(3);

    companion object {
        const val TOTAL_COUNT = 4

        fun getForIndex(index: Int) = values()[index]

        fun calculateCurrentWeekNumber(): WeekNumber {
            val currentCalendar = Calendar.getInstance().apply {
                set(HOUR_OF_DAY, 0)
                set(MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(MILLISECOND, 0)
            }
            val firstDayYear = currentCalendar[YEAR].run {
                if (currentCalendar[MONTH] < AUGUST) this - 1 else this
            }
            val firstDayCalendar = Calendar.getInstance().apply {
                set(firstDayYear, SEPTEMBER, 1, 0, 0, 0)
                set(MILLISECOND, 0)
            }
            if (currentCalendar < firstDayCalendar) {
                currentCalendar[YEAR] = firstDayYear + 1
            }
            val difference = (currentCalendar.timeInMillis - firstDayCalendar.timeInMillis) / 1000 / 60 / 60 / 24
            val day = firstDayCalendar.get(DAY_OF_WEEK).minus(2).run {
                if (this == -1) 6 else this
            }
            val weekNumberIndex = ((difference.toInt() + day) / 7) % 4
            return getForIndex(weekNumberIndex)
        }
    }

    fun getNext(): WeekNumber {
        val nextIndex = (index + 1) % TOTAL_COUNT
        return getForIndex(nextIndex)
    }
}