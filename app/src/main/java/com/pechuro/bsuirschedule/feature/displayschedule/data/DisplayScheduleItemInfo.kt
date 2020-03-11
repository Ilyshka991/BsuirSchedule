package com.pechuro.bsuirschedule.feature.displayschedule.data

import android.os.Parcelable
import com.pechuro.bsuirschedule.domain.entity.WeekDay
import com.pechuro.bsuirschedule.domain.entity.WeekNumber
import kotlinx.android.parcel.Parcelize

sealed class DisplayScheduleItemInfo : Parcelable {

    @Parcelize
    data class DayClasses(
            val weekDay: WeekDay,
            val weekNumber: WeekNumber
    ) : DisplayScheduleItemInfo()

    @Parcelize
    data class WeekClasses(
            val weekDay: WeekDay
    ) : DisplayScheduleItemInfo()

    @Parcelize
    object Exams : DisplayScheduleItemInfo()
}