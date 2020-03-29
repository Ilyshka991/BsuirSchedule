package com.pechuro.bsuirschedule.domain.entity

interface ILesson : IScheduleItem {
    val weekDay: WeekDay
    val weekNumber: WeekNumber
    val priority: LessonPriority
}