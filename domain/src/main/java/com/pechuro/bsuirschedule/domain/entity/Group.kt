package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
        val id: Long,
        val number: String,
        val faculty: Faculty,
        val speciality: Speciality,
        val course: Int
) : Parcelable

val Group.availableScheduleTypes: Iterable<ScheduleType>
    get() = when {
        speciality.educationForm.name == "заочная" -> listOf(ScheduleType.EXAMS)
        else -> ScheduleType.values().toList()
    }