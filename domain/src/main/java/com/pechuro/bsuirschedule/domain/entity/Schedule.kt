package com.pechuro.bsuirschedule.domain.entity

import java.util.*

sealed class Schedule(
        val name: String
) {

    class GroupClasses(
            name: String,
            val lastUpdated: Date,
            val group: Group
    ) : Schedule(
            name = name
    )

    class GroupExams(
            name: String,
            val lastUpdated: Date,
            val group: Group
    ) : Schedule(
            name = name
    )

    class EmployeeClasses(
            name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )

    class EmployeeExams(
            name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )
}