package com.pechuro.bsuirschedule.utils

import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.SCHEDULES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_CLASSES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.entity.Schedule

fun List<Schedule>.toMap(): Map<Int, List<String>> {
    val map = mutableMapOf<Int, List<String>>()

    map[SCHEDULES] =
            this.asSequence()
                    .filter {
                        it.type == STUDENT_CLASSES ||
                                it.type == EMPLOYEE_CLASSES
                    }
                    .map { it.name }
                    .toList()

    map[EXAMS] =
            this.asSequence()
                    .filter {
                        it.type == STUDENT_EXAMS ||
                                it.type == EMPLOYEE_EXAMS
                    }
                    .map { it.name }
                    .toList()
    return map
}