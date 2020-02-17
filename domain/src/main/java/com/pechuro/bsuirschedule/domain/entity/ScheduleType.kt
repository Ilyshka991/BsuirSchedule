package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class ScheduleType : Parcelable {
    CLASSES, EXAMS
}