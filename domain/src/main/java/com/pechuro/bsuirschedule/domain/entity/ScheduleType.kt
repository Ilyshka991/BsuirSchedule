package com.pechuro.bsuirschedule.domain.entity

enum class ScheduleType(val value: Int) {
    CLASSES(1), EXAMS(2);

    companion object {

        fun getOrException(type: Int) = when (type) {
            1 -> CLASSES
            2 -> EXAMS
            else -> throw IllegalArgumentException("Illegal value: $type")
        }
    }
}