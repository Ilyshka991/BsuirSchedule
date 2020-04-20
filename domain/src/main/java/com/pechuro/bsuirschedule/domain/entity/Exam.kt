package com.pechuro.bsuirschedule.domain.entity

import kotlinx.android.parcel.Parcelize

sealed class Exam(
        open val date: LocalDate
) : ScheduleItem {

    @Parcelize
    data class GroupExam(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: LocalTime,
            override val endTime: LocalTime,
            override val auditories: List<Auditory>,
            override val isAddedByUser: Boolean,
            override val date: LocalDate,
            val employees: List<Employee>
    ) : Exam(
            date = date
    )

    @Parcelize
    data class EmployeeExam(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: LocalTime,
            override val endTime: LocalTime,
            override val auditories: List<Auditory>,
            override val isAddedByUser: Boolean,
            override val date: LocalDate,
            val studentGroups: List<Group>
    ) : Exam(
            date = date
    )
}

