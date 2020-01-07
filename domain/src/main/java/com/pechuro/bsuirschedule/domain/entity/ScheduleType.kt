package com.pechuro.bsuirschedule.domain.entity

enum class ScheduleType(val value: Int) {
    STUDENT_CLASSES(1), STUDENT_EXAMS(2),
    EMPLOYEE_CLASSES(3), EMPLOYEE_EXAMS(4);

    companion object {

        fun getOrException(type: Int) = when (type) {
            1 -> STUDENT_CLASSES
            2 -> STUDENT_EXAMS
            3 -> EMPLOYEE_CLASSES
            4 -> EMPLOYEE_EXAMS
            else -> throw IllegalArgumentException("Illegal value: $type")
        }
    }
}