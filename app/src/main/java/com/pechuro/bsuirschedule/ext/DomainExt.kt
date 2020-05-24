package com.pechuro.bsuirschedule.ext

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.*
import java.util.*

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

val LocalTime.formattedString: String
    get() = String.format("%02d:%02d", hour, minute)

val LocalDate.formattedString: String
    get() = String.format("%02d.%02d.%d", day, month + 1, year)

@get:StringRes
val AppTheme.formattedStringRes: Int
    get() = when (this) {
        AppTheme.FOLLOW_SYSTEM -> R.string.theme_follow_system
        AppTheme.LIGHT -> R.string.theme_light
        AppTheme.DARK -> R.string.theme_dark
        AppTheme.BLACK -> R.string.theme_black
        AppTheme.INDIGO -> R.string.theme_indigo
        AppTheme.TEAL -> R.string.theme_teal
        AppTheme.BLUE_GREY -> R.string.theme_blue_grey
        AppTheme.BLUE_LIGHT -> R.string.theme_blue_white
        AppTheme.BROWN -> R.string.theme_brown
        AppTheme.ORANGE -> R.string.theme_orange
        AppTheme.PURPLE -> R.string.theme_purple
        AppTheme.CYAN -> R.string.theme_cyan
        AppTheme.RED -> R.string.theme_red
        AppTheme.LIME -> R.string.theme_lime
    }

@get:StringRes
val ScheduleWidgetInfo.WidgetTheme.formattedStringRes: Int
    get() = when (this) {
        ScheduleWidgetInfo.WidgetTheme.SYSTEM -> R.string.theme_follow_system
        ScheduleWidgetInfo.WidgetTheme.LIGHT -> R.string.theme_light
        ScheduleWidgetInfo.WidgetTheme.DARK -> R.string.theme_dark
    }

val ScheduleItem.isExam: Boolean
    get() = lessonType.toLowerCase(Locale.getDefault()) == "экзамен"

val ScheduleItem.isConsultation: Boolean
    get() = lessonType.toLowerCase(Locale.getDefault()) == "консультация"