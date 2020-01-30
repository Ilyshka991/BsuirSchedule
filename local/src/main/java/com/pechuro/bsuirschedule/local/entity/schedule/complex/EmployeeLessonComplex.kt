package com.pechuro.bsuirschedule.local.entity.schedule.complex

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.pechuro.bsuirschedule.local.entity.building.AuditoryCached
import com.pechuro.bsuirschedule.local.entity.schedule.EmployeeLessonCached
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.EmployeeLessonGroupCrossRef
import com.pechuro.bsuirschedule.local.entity.schedule.crossref.GroupLessonAuditoryCrossRef
import com.pechuro.bsuirschedule.local.entity.staff.GroupCached

data class EmployeeLessonComplex(
        @Embedded
        val scheduleItem: EmployeeLessonCached,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "auditory_id",
                associateBy = Junction(GroupLessonAuditoryCrossRef::class)
        )
        val auditories: List<AuditoryCached>,
        @Relation(
                parentColumn = "schedule_item_id",
                entityColumn = "group_id",
                associateBy = Junction(EmployeeLessonGroupCrossRef::class)
        )
        val groups: List<GroupCached>
)