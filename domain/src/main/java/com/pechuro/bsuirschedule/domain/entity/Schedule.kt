package com.pechuro.bsuirschedule.domain.entity

import java.util.*

sealed class Schedule(
        open val name: String
) {

    data class GroupClasses(
            override val name: String,
            val lastUpdateDate: Date,
            val group: Group
    ) : Schedule(
            name = name
    )

    data class GroupExams(
            override val name: String,
            val lastUpdated: Date,
            val group: Group
    ) : Schedule(
            name = name
    )

    data class EmployeeClasses(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )

    data class EmployeeExams(
            override val name: String,
            val employee: Employee
    ) : Schedule(
            name = name
    )
}