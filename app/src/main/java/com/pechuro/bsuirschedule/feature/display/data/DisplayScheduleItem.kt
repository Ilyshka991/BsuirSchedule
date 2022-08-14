package com.pechuro.bsuirschedule.feature.display.data

import android.os.Parcelable
import com.pechuro.bsuirschedule.domain.entity.Exam
import com.pechuro.bsuirschedule.domain.entity.Lesson
import com.pechuro.bsuirschedule.domain.entity.ScheduleItem
import kotlinx.android.parcel.Parcelize

sealed class DisplayScheduleItem(
    open val scheduleItem: ScheduleItem?
) : Parcelable {

    @Parcelize
    data class DayClasses(
        override val scheduleItem: Lesson
    ) : DisplayScheduleItem(scheduleItem)

    @Parcelize
    data class WeekClasses(
        override val scheduleItem: Lesson,
        val allScheduleItems: List<Lesson>
    ) : DisplayScheduleItem(scheduleItem)

    @Parcelize
    data class Exams(
        override val scheduleItem: Exam
    ) : DisplayScheduleItem(scheduleItem)

    @Parcelize
    object Empty : DisplayScheduleItem(null)
}