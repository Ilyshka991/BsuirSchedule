package com.pechuro.bsuirschedule.domain.entity

import kotlinx.android.parcel.Parcelize
import java.util.*

sealed class Exam(
        open val date: Date
) : ScheduleItem {

    @Parcelize
    data class GroupExam(
            override val id: Long,
            override val subject: String,
            override val subgroupNumber: SubgroupNumber,
            override val lessonType: String,
            override val note: String,
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
            override val date: Date,
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
            override val startTime: String,
            override val endTime: String,
            override val auditories: List<Auditory>,
            override val date: Date,
            val studentGroups: List<Group>
    ) : Exam(
            date = date
    )
}

