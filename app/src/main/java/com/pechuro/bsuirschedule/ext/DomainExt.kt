package com.pechuro.bsuirschedule.ext

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.*

@get:ColorRes
val LessonPriority.formattedColorRes: Int
    get() = when (this) {
        LessonPriority.LOWEST -> R.color.amber_200
        LessonPriority.LOW -> R.color.amber_400
        LessonPriority.MEDIUM -> R.color.amber_600
        LessonPriority.HIGH -> R.color.amber_800
    }

@get:StringRes
val LessonPriority.formattedStringRes: Int
    get() = when (this) {
        LessonPriority.LOWEST -> R.string.lesson_priotity_lowest
        LessonPriority.LOW -> R.string.lesson_priotity_low
        LessonPriority.MEDIUM -> R.string.lesson_priotity_medium
        LessonPriority.HIGH -> R.string.lesson_priotity_high
    }

@get:StringRes
val SubgroupNumber.formattedStringRes: Int
    get() = when (this) {
        SubgroupNumber.ALL -> R.string.subgroup_number_all
        SubgroupNumber.FIRST -> R.string.subgroup_number_first
        SubgroupNumber.SECOND -> R.string.subgroup_number_second
    }

fun WeekDay.getFormattedString(resources: Resources): String {
    val resourceArray = resources.getStringArray(R.array.weekdays)
    return resourceArray[index]
}

val Auditory.formattedName: String
    get() = "$name-${building.name}"

val WeekNumber.formattedString: String
    get() = (index + 1).toString()