package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable

interface ScheduleItem : Parcelable {
    val id: Long
    val subject: String
    val subgroupNumber: SubgroupNumber
    val lessonType: String
    val note: String
    val startTime: String
    val endTime: String
    val auditories: List<Auditory>
    val isAddedByUser: Boolean
}