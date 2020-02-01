package com.pechuro.bsuirschedule.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ScheduleItemComplex(
        @Embedded
        val scheduleItem: ScheduleItemCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "employee_id",
                associateBy = Junction(ScheduleItemEmployeeCrossRef::class)
        )
        val employees: List<EmployeeCached>?,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(ScheduleItemAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>?,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "group_id",
                associateBy = Junction(ScheduleItemGroupCrossRef::class)
        )
        val groups: List<GroupCached>?
)