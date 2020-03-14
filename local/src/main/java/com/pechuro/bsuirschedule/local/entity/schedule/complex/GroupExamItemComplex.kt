package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamEmployeeCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached

data class GroupExamItemComplex(
        @Embedded
        val scheduleItem: GroupItemExamCached,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = GroupExamEmployeeCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "employee_id"
                )
        )
        val employees: List<EmployeeCached>,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = GroupExamAuditoryCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "auditory_id"
                )
        )
        val auditories: List<AuditoryCached>
)