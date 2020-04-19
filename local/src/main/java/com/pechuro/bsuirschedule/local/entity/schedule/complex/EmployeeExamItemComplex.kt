package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeExamGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

data class EmployeeExamItemComplex(
        @Embedded
        val scheduleItem: EmployeeItemExamCached,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = EmployeeExamAuditoryCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "auditory_id"
                )
        )
        val auditories: List<AuditoryCached>,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = EmployeeExamGroupCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "group_id"
                )
        )
        val groups: List<GroupCached>
)