package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Speciality(
    val id: Long,
    val faculty: Faculty?,
    val educationForm: EducationForm,
    var name: String,
    val abbreviation: String,
    val code: String
) : Parcelable