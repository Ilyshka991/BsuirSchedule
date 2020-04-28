package com.pechuro.bsuirschedule.local.sharedprefs

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalScheduleInfo(
        val name: String,
        val type: ScheduleType
) {
    @Keep
    enum class ScheduleType {
        GROUP_CLASSES, GROUP_EXAMS, EMPLOYEE_CLASSES, EMPLOYEE_EXAMS
    }
}