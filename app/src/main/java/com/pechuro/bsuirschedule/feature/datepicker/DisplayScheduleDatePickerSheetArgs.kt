package com.pechuro.bsuirschedule.feature.datepicker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class DisplayScheduleDatePickerSheetArgs(
    val startDate: Date,
    val endDate: Date,
    val currentDate: Date
) : Parcelable