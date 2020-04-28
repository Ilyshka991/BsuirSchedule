package com.pechuro.bsuirschedule.feature.lessondetails

import com.pechuro.bsuirschedule.domain.entity.Auditory
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.WeekNumber

sealed class DetailsItem {

    companion object {
        const val LESSON_HEADER_VIEW_TYPE = 0
        const val LOCATION_VIEW_TYPE = 1
    }

    open val id: Any get() = this

    val viewType: Int
        get() = when (this) {
            is LessonHeader -> LESSON_HEADER_VIEW_TYPE
            is LocationItem -> LOCATION_VIEW_TYPE
        }

    data class LessonHeader(
            val lesson: Lesson,
            val weeks: List<WeekNumber>
    ) : DetailsItem() {
        override val id: Any get() = lesson.id
    }

    data class LocationItem(val auditory: Auditory) : DetailsItem() {
        override val id: Any get() = auditory.id
    }
}
