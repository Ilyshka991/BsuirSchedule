package com.pechuro.bsuirschedule.feature.displayschedule.data

import com.pechuro.bsuirschedule.domain.entity.ILesson
import com.pechuro.bsuirschedule.domain.entity.LessonPriority
import com.pechuro.bsuirschedule.domain.entity.SubgroupNumber

data class LessonGroupKey(
        val subject: String,
        val subgroupNumber: SubgroupNumber,
        val lessonType: String,
        val startTime: String,
        val endTime: String,
        val priority: LessonPriority
)

fun ILesson.toGroupKey() = LessonGroupKey(
        subject = subject,
        subgroupNumber = subgroupNumber,
        lessonType = lessonType,
        startTime = startTime,
        endTime = endTime,
        priority = priority
)