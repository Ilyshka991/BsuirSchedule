package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Department(
    val id: Long,
    var name: String,
    val abbreviation: String
) : Parcelable