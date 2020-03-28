package com.pechuro.bsuirschedule.domain.entity

import java.util.*

enum class LessonPriority(val value: Int) {
    LOWEST(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    companion object {

        fun getForValue(value: Int) = values().find { it.value == value }
                ?: throw IllegalArgumentException("No priority for $value found")

        fun getDefaultForLessonType(lessonType: String) = when (lessonType.toUpperCase(Locale.getDefault())) {
            "ЛК" -> LOW
            "ПЗ" -> MEDIUM
            "ЛР" -> HIGH
            else -> MEDIUM
        }
    }
}