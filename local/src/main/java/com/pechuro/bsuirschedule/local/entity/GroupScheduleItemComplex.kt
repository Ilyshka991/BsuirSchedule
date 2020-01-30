package com.pechuro.bsuirschedule.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupScheduleItemComplex(
        @Embedded
        val scheduleItem: GroupScheduleItemCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "employee_id",
                associateBy = Junction(GroupScheduleItemEmployeeCrossRef::class)
        )
        val employees: List<EmployeeCached>?,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(GroupScheduleItemAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>?
)