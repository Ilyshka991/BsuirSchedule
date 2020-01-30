package com.pechuro.bsuirschedule.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EmployeeScheduleItemComplex(
        @Embedded
        val scheduleItem: EmployeeScheduleItemCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(GroupScheduleItemAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>?,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "group_id",
                associateBy = Junction(EmployeeScheduleItemGroupCrossRef::class)
        )
        val groups: List<GroupCached>?
)