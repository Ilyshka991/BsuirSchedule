package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeItemClassesCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

data class EmployeeClassesItemComplex(
        @Embedded
        val scheduleItem: EmployeeItemClassesCached,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = EmployeeLessonAuditoryCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "auditory_id"
                )
        )
        val auditories: List<AuditoryCached>,
        @Relation(
                parentColumn = "id",
                entityColumn = "id",
                associateBy = Junction(
                        value = EmployeeLessonGroupCrossRef::class,
                        parentColumn = "schedule_item_id",
                        entityColumn = "group_id"
                )
        )
        val groups: List<GroupCached>
)