package com.pechuro.bsuirschedule.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

sealed class Announcement(
    open val date: Date,
    open val content: String
) : Parcelable {

    @Parcelize
    class EmployeeAnnouncement(
        override val date: Date,
        override val content: String,
        val employee: Employee
    ) : Announcement(
        date = date,
        content = content
    )

    @Parcelize
    class DepartmentAnnouncement(
        override val date: Date,
        override val content: String,
        val employee: Department
    ) : Announcement(
        date = date,
        content = content
    )
}
