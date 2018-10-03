package com.pechuro.bsuirschedule.utils

import com.pechuro.bsuirschedule.constant.ScheduleType.EMPLOYEE_EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.EXAMS
import com.pechuro.bsuirschedule.constant.ScheduleType.SCHEDULES
import com.pechuro.bsuirschedule.constant.ScheduleType.STUDENT_EXAMS
import com.pechuro.bsuirschedule.data.entity.Schedule

fun List<Schedule>.toMap(): Map<Int, List<String>> {
    val map = mutableMapOf<Int, List<String>>()

    val list = mutableListOf<String>()
    for (i in 1..16) {
        list.add(i.toString())
    }
    map[SCHEDULES] = list
    /*this.asSequence()
            .filter {
                it.type == STUDENT_CLASSES ||
                        it.type == EMPLOYEE_CLASSES
            }
            .map { it.name }
            .toList()*/

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