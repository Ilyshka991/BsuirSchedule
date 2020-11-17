package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Employee(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val middleName: String,
        val abbreviation: String,
        val photoLink: String,
        val rank: String,
        val department: Department?
) : Parcelable
