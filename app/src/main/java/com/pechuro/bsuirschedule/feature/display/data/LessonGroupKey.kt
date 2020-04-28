package com.pechuro.bsuirschedule.feature.display.data

import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.LocalTime
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber

data class LessonGroupKey(
        val subject: String,
        val subgroupNumber: SubgroupNumber,
        val lessonType: String,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val priority: LessonPriority
)

fun Lesson.toGroupKey() = LessonGroupKey(
        subject = subject,
        subgroupNumber = subgroupNumber,
        lessonType = lessonType,
        startTime = startTime,
        endTime = endTime,
        priority = priority
)