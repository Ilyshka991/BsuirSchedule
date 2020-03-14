package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.GroupItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonEmployeeCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.EmployeeCached

data class GroupClassesItemComplex(
        @Embedded
        val scheduleItem: GroupItemClassesCached,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = GroupLessonEmployeeCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "employee_id"
                )
        )
        val employees: List<EmployeeCached>,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = GroupLessonAuditoryCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "auditory_id"
                )
        )
        val auditories: List<AuditoryCached>
)