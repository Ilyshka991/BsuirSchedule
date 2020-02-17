package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

sealed class Schedule(
        open val name: String
) : Parcelable {

    @Parcelize
    data class GroupClasses(
            override val name: String,
            val lastUpdatedDate: Date,
            val group: Group,
            val notRemindForUpdates: Boolean
    ) : Schedule(
            name = name
    )

    @Parcelize
    data class GroupExams(
            override val name: String,
            val lastUpdatedDate: Date,
            val group: Group,
            val notRemindForUpdates: Boolean
    ) : Schedule(
            name = name
    )

    @Parcelize
    data class EmployeeClasses(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )

    @Parcelize
    data class EmployeeExams(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )
}