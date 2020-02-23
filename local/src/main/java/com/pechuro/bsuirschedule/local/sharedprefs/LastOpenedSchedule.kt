package com.pechuro.bsuirschedule.local.sharedprefs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastOpenedSchedule(
        val name: String,
        val type: ScheduleType
) {
    enum class ScheduleType {
        GROUP_CLASSES, GROUP_EXAMS, EMPLOYEE_CLASSES, EMPLOYEE_EXAMS
    }
}