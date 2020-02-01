package com.pechuro.bsuirschedule.domain.entity

import java.util.*

sealed class Announcement(
        val date: Date,
        val content: String
) {
    class EmployeeAnnouncement(
            date: Date,
            content: String,
            val employee: Employee
    ) : Announcement(
            date = date,
            content = content
    )

    class DepartmentAnnouncement(
            date: Date,
            content: String,
            val employee: Department
    ) : Announcement(
            date = date,
            content = content
    )
}
