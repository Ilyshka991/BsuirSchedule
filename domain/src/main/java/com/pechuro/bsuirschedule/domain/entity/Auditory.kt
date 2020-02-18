package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Auditory(
        val id: Long,
        val name: String,
        val note: String,
        val capacity: Int,
        val building: Building,
        val auditoryType: AuditoryType,
        val department: Department?
) : Parcelable