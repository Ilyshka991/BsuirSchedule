package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupExamCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupExamEmployeeCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached

data class GroupExamComplex(
        @Embedded
        val scheduleItem: GroupExamCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "employee_id",
                associateBy = Junction(GroupExamEmployeeCrossRef::class)
        )
        val employees: List<EmployeeCached>,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(GroupExamAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>
)