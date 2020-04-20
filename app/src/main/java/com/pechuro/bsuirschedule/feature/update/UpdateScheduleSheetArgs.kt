package com.pechuro.bsuirschedule.feature.update

import android.os.Parcelable
import com.pechuro.bsuirschedule.domain.entity.Schedule
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateScheduleSheetArgs(
        val schedules: List<Schedule>
) : Parcelable