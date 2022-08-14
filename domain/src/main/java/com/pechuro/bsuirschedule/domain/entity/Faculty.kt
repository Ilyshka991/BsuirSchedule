package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Faculty(
    val id: Long,
    val name: String,
    val abbreviation: String
) : Parcelable