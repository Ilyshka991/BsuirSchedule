package com.pechuro.bsuirschedule.feature.itemdetails

import android.os.Parcelable
import com.pechuro.bsuirschedule.domain.entity.Schedule
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleItemDetailsArgs(
        val schedule: Schedule,
        val itemId: Long
) : Parcelable