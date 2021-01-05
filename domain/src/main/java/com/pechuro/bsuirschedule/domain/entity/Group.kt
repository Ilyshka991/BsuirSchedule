package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import com.pechuro.bsuirschedule.domain.ext.addIfEmpty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
        val id: Long,
        val number: String,
        val speciality: Speciality,
        val course: Int
) : Parcelable

val Group.availableScheduleTypes: Iterable<ScheduleType>
    get() = when {
        speciality.educationForm.isPartTime -> listOf(ScheduleType.EXAMS)
        else -> ScheduleType.values().toList()
    }

fun Group.correlateScheduleTypes(typesToDownload: List<ScheduleType>): List<ScheduleType> {
    val availableTypes = availableScheduleTypes
    return typesToDownload
            .intersect(availableTypes)
            .addIfEmpty {
                availableTypes.firstOrNull()
            }
            .filterNotNull()
            .toList()
}