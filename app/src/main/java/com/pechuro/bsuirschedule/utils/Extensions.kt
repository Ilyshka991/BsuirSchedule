package com.pechuro.bsuirschedule.utils

import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleTypes.SCHEDULES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleTypes.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.entity.Schedule
import com.pechuro.bsuirschedule.ui.activity.navigation.transactioninfo.ScheduleInformation
import java.util.*
import java.util.Calendar.*
import java.util.GregorianCalendar.DAY_OF_WEEK

fun List<Schedule>.toMap(): Map<Int, List<ScheduleInformation>> {
    val map = mutableMapOf<Int, List<ScheduleInformation>>()

    map[SCHEDULES] =
            this.asSequence()
                    .filter {
                        it.type == STUDENT_CLASSES ||
                                it.type == EMPLOYEE_CLASSES
                    }
                    .map { ScheduleInformation(it.name, it.type) }
                    .toList()

    map[EXAMS] =
            this.asSequence()
                    .filter {
                        it.type == STUDENT_EXAMS ||
                                it.type == EMPLOYEE_EXAMS
                    }
                    .map { ScheduleInformation(it.name, it.type) }
                    .toList()
    return map
}

fun List<Schedule>.transform(): List<ScheduleInformation> {
    val result = mutableListOf<ScheduleInformation>()
    this.forEach { result.add(ScheduleInformation(it.name, it.type)) }
    return result
}

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
