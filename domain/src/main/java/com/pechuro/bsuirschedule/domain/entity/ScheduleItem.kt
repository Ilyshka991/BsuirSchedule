package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable

interface ScheduleItem : Parcelable {
    val id: Long
    val subject: String
    val subgroupNumber: SubgroupNumber
    val lessonType: String
    val note: String
    val startTime: LocalTime
    val endTime: LocalTime
    val auditories: List<Auditory>
    val isAddedByUser: Boolean
}