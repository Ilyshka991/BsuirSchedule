package com.pechuro.bsuirschedule.ui.utils

import java.util.*
import java.util.Calendar.*
import java.util.GregorianCalendar.DAY_OF_WEEK

fun Calendar.getCurrentWeek(): Int {
    val currentDate = this
    var year = this.get(YEAR)

    if (this.get(MONTH) < 8) {
        year--
    }

    val firstDay = Calendar.getInstance()
    firstDay.set(year, SEPTEMBER, 1, 0, 0, 0)

    val difference = (currentDate.timeInMillis - firstDay.timeInMillis) / 1000 / 60 / 60 / 24
    var day = firstDay.get(DAY_OF_WEEK)

    day -= 2
    if (day == -1) {
        day = 6
    }
    return ((difference + day).toInt() / 7) % 4 + 1
}

fun Calendar.addDays(days: Int): Long {
    this.add(Calendar.DATE, days)
    return this.time.time
}
