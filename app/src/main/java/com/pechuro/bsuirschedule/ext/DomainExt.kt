package com.pechuro.bsuirschedule.ext

import androidx.annotation.ColorRes
import com.pechuro.bsuirschedule.R
import com.pechuro.bsuirschedule.domain.entity.LessonPriority

@get:ColorRes
val LessonPriority.colorRes: Int
    get() = when (this) {
        LessonPriority.LOWEST -> R.color.amber_200
        LessonPriority.LOW -> R.color.amber_400
        LessonPriority.MEDIUM -> R.color.amber_600
        LessonPriority.HIGH -> R.color.amber_800
    }