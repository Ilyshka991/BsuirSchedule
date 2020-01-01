package com.pechuro.bsuirschedule.domain.entity

enum class ScheduleType(val type: Int) {
    STUDENT_CLASSES(1), STUDENT_EXAMS(2),
    EMPLOYEE_CLASSES(3), EMPLOYEE_EXAMS(4)
}