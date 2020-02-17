package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EducationForm(
        val id: Long,
        val name: String
) : Parcelable