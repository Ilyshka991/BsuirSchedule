package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

data class EmployeeExamComplex(
        @Embedded
        val scheduleItem: EmployeeExamCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(GroupExamAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "group_id",
                associateBy = Junction(EmployeeExamGroupCrossRef::class)
        )
        val groups: List<GroupCached>
)